package com.yuan.im.netty.codec.online.client.one;

import com.yuan.im.netty.codec.online.vo.ImModel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/19 4:45 下午
 * @describe: ClientChannelInitializer
 */
public class ClientChannelInitializerA extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 设置log监听器
        pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
        // 在pipeline加入ProtoBufDecoder, 指定对哪种对象进行解码
        pipeline.addLast("decoder", new ProtobufDecoder(ImModel.ImDataModel.getDefaultInstance()));
        // 在pipeline中加入 ProtoBufEncoder,设置Protobuf编码器
        pipeline.addLast("encoder", new ProtobufEncoder());
        // 添加自定义业务handler
        pipeline.addLast(new ProtoNettyClientHandlerA());
    }
}
