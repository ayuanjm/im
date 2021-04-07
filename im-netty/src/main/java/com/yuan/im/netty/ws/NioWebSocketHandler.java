package com.yuan.im.netty.ws;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/1 11:09 上午
 * @describe: 自定义业务处理器
 */
public class NioWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(NioWebSocketHandler.class);
    /**
     * 请求头
     */
    private static final String REQUEST_HEADERS = "websocket";

    /**
     * 升级请求
     */
    private static final String REQUEST_HEADERS_NAME = "Upgrade";

    private WebSocketServerHandshaker handShaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        log.info("收到消息:{}", JSON.toJSONString(msg));
        if (msg instanceof FullHttpRequest) {
            // 以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端加入连接:{}", ctx.channel());
        ChannelSupervise.addChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端断开连接:{}", ctx.channel());
        ChannelSupervise.removeChannel(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
            return;
        }

        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (!(frame instanceof TextWebSocketFrame)) {
            log.info("当前版本只支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }

        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        log.info("服务端接收到请求:" + request);
        TextWebSocketFrame tws = new TextWebSocketFrame("date: " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()) + "<br>id: " + ctx.channel().id() + "<br>内容: " + request);
        // 群发
        ChannelSupervise.sendToAll(tws);
    }

    /**
     * http请求转websocket请求
     *
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 要求Upgrade为websocket，过滤get/post
        if (req.decoderResult().isFailure() || (!REQUEST_HEADERS.equals(req.headers().get(REQUEST_HEADERS_NAME)))) {
            // 不是websocket方式，创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8088/websocket", null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
    }

    /**
     * 拒绝不合法的请求，返回错误信息
     *
     * @param ctx
     * @param req
     * @param res
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        if (res.status().code() != HttpResponseStatus.OK.code()) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(byteBuf);
            byteBuf.release();
        }
        // 返回应答给客户端
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(res);
        // 连接失效，关闭连接
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != HttpResponseStatus.OK.code()) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
