package com.yuan.im.netty.codec.online.client.one;

import com.yuan.im.netty.codec.online.client.ClientChannelInitializer;
import com.yuan.im.netty.codec.online.vo.ImRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Random;
import java.util.Scanner;

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
                    // 创建一个通道处理对象
                    .handler(new ClientChannelInitializer());
            System.out.println("客户端 is ok ...");
            // 启动客户端去连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8088).sync();
            sendMsg(channelFuture);
            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    private static void sendMsg(ChannelFuture channelFuture) {
        // 得到channel
        Channel channel = channelFuture.channel();
        System.out.println("-------" + channel.localAddress() + "--------");
        // 客户端需要输入信息，创建一个扫描器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            // 随机发送User或者Worker对象
            int nextInt = new Random().nextInt(3);
            ImRequest.ImRequestInfo message = null;
            if (0 == nextInt) {
                message = ImRequest.ImRequestInfo.newBuilder().setDataType(ImRequest.ImRequestInfo.DataType.SendRequestType)
                        .setSendRequest(ImRequest.SendRequest.newBuilder()
                                .setFrom(1L)
                                .setGroup("ab")
                                .setContent(msg)
                                .setType(0)
                                .setCmd(1)
                                .build()).build();
            } else {
                message = ImRequest.ImRequestInfo.newBuilder().setDataType(ImRequest.ImRequestInfo.DataType.PullRequestType)
                        .setPullRequest(ImRequest.PullRequest.newBuilder()
                                .setUid(1L)
                                .setGroup("ab")
                                .setLimit(10)
                                .setOffset(0L)
                                .build()).build();
            }
            // 通过channel 发送到服务器端
            channel.writeAndFlush(message);
        }
    }
}
