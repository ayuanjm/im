package com.yuan.im.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/10 11:01 下午
 * @describe:
 */
public class CustomServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();
        /**
         * 加入一个Netty提供的HttpServerCodec，codec =>{coder - decoder}
         * HttpServerCodec是Netty提供的处理http的 编-解码器
         */
        pipeline.addLast("myHttpServerCodec", new HttpServerCodec());
        // 增加一个自定义的Handler
        pipeline.addLast("myHttpServerHandler", new HttpServerHandler());
    }
}
