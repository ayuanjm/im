package com.yuan.im.netty.codec.online;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @desc:
 * @author: YuanJiaMin
 * @date: 2021/4/6 23:38
 */
public class ProtoNettyClient {
    public static void main(String[] args) throws InterruptedException {
        // 客户端需要一个事件循环组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();
            // 设置相关参数
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 在pipeline中加入 ProtoBufEncoder
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast(new ProtoNettyClientHandler());
                        }
                    });
            System.out.println("客户端 is ok ...");
            // 启动客户端去连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8088).sync();
            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
