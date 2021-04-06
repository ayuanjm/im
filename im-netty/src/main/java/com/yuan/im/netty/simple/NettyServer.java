package com.yuan.im.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @desc:
 * @author: YuanJiaMin
 * @date: 2021/4/6 22:52
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建bossGroup和workGroup
         * 1、创建两个线程组bossGroup和workGroup
         * 2、bossGroup只是处理连接请求，真正和客户端业务处理会交给workGroup完成
         * 3、两个都是无限循环
         * 4、bossGroup和workGroup含有的子线程（NioEventLoop）的个数，默认2*cup核数
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置两个线程组
            serverBootstrap.group(bossGroup, workGroup)
                    // 使用NioServerSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列，得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 创建一个通道处理匿名对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 给pipeline设置处理器
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("服务器 start-up ...");
            // 绑定一个端口并且同步，生成了一个channelFuture对象，启动服务器并绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
