package com.yuan.im.netty.codec.online.server;

import com.yuan.im.netty.codec.online.vo.ImModel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
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
        // 聚合器，websocket会用到
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        // 用于大数据的分区传输
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast(new IdleStateHandler(15, 20, 10, TimeUnit.MINUTES));
        // 给pipeline设置处理器
        pipeline.addLast(new ProtoNettyServerHandler());
    }
}
