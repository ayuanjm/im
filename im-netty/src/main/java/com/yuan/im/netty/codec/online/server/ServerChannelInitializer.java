package com.yuan.im.netty.codec.online.server;

import com.yuan.im.netty.codec.online.vo.ImModel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P>https://segmentfault.com/a/1190000017464313</P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/19 4:44 下午
 * @describe: ServerChannelInitializer
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("客户socketChannel hashCode:" + ch.hashCode());

        ChannelPipeline pipeline = ch.pipeline();
        // 设置log监听器
        pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
        // 在pipeline加入ProtoBufDecoder, 指定对哪种对象进行解码
        pipeline.addLast("decoder", new ProtobufDecoder(ImModel.ImDataModel.getDefaultInstance()));
        // 在pipeline中加入 ProtoBufEncoder, 设置Protobuf编码器
        pipeline.addLast("encoder", new ProtobufEncoder());
        // 心跳检测
        pipeline.addLast(new IdleStateHandler(15, 20, 10, TimeUnit.MINUTES));
        // 给pipeline设置处理器
        pipeline.addLast(new ProtoNettyServerHandler());

        //        // HTTP请求的解码和编码
//        pipeline.addLast(new HttpServerCodec());
//        // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse，
//        // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
//        pipeline.addLast(new HttpObjectAggregator(65536));
//        // 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的; 增加之后就不用考虑这个问题了
//        pipeline.addLast(new ChunkedWriteHandler());
//        // WebSocket数据压缩
//        pipeline.addLast(new WebSocketServerCompressionHandler());
//        // 协议包长度限制
//        pipeline.addLast(new WebSocketServerProtocolHandler("", null, true));
//        // 协议包解码
//        pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
//            @Override
//            protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
//                ByteBuf buf = ((BinaryWebSocketFrame) frame).content();
//                objs.add(buf);
//                buf.retain();
//            }
//        });
//        // 协议包编码
//        pipeline.addLast(new MessageToMessageEncoder<MessageLiteOrBuilder>() {
//            @Override
//            protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
//                ByteBuf result = null;
//                if (msg instanceof MessageLite) {
//                    result = wrappedBuffer(((MessageLite) msg).toByteArray());
//                }
//                if (msg instanceof MessageLite.Builder) {
//                    result = wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
//                }
//                // 然后下面再转成websocket二进制流，因为客户端不能直接解析protobuf编码生成的
//                WebSocketFrame frame = new BinaryWebSocketFrame(result);
//                out.add(frame);
//            }
//        });
    }
}
