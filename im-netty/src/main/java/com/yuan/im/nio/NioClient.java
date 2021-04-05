package com.yuan.im.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/5 2:24 下午
 * @describe:
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        Thread a = new Thread(() -> {
            try {
                client();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread b = new Thread(() -> {
            try {
                client();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        a.start();
        b.start();
    }

    private static void client() throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        // 提供服务器ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8088);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        // 如果连接成功发送数据
        String msg = "hello world 中文";
        // 按发送数据的大小，创建ByteBuffer内部数组大小；
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        // 发送数据，将buffer的数据写入channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
