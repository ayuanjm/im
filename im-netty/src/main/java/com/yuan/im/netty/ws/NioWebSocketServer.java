package com.yuan.im.netty.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/1 10:47 上午
 * @describe: websocket 服务端
 */
public class NioWebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(NioWebSocketServer.class);

    public static void main(String[] args) {
        new NioWebSocketServer().init();
    }

    /**
     * websocket init
     */
    private void init() {
        log.info("正在启动websocket服务器");
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, work);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new NioWebSocketChannelInitializer());
        try {
            Channel channel = serverBootstrap.bind(8088).sync().channel();
            log.info("websocket服务器启动成功:{}", channel);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("运行出错:" + e);
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("websocket服务器已关闭");
        }
    }
}
