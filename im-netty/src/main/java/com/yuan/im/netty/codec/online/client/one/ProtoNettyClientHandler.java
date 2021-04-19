package com.yuan.im.netty.codec.online.client.one;

import com.yuan.im.netty.codec.online.vo.ImRequest;
import com.yuan.im.netty.codec.online.vo.ImResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @desc:
 * @author: YuanJiaMin
 * @date: 2021/4/6 23:46
 */
public class ProtoNettyClientHandler extends SimpleChannelInboundHandler<ImResponse.ImResponseInfo> {

    /**
     * 当通道就绪就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ImRequest.ImRequestInfo message = ImRequest.ImRequestInfo.newBuilder().setDataType(ImRequest.ImRequestInfo.DataType.SendRequestType)
                .setSendRequest(ImRequest.SendRequest.newBuilder()
                        .setFrom(1L)
                        .setGroup("ab")
                        .setCmd(0)
                        .build()).build();
        ctx.writeAndFlush(message);
    }

    /**
     * 当通道有读取事件时会触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImResponse.ImResponseInfo msg) throws Exception {
        ImResponse.PullResponse pullResponse = msg.getPullResponse();
        System.out.println(pullResponse.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
