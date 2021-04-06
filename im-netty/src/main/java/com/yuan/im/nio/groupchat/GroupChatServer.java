package com.yuan.im.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/5 3:48 下午
 * @describe:
 */
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 8088;

    /**
     * 初始化
     */
    private void init() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    private void listen() {
        try {
            while (true) {
                int count = selector.select(10000);
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + " 上线");
                        }
                        if (key.isReadable()) {
                            readData(key);
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待连接...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 读取客户端消息
     *
     * @param key
     */
    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(buffer);
            if (count > 0) {
                buffer.flip();
                String msg = new String(buffer.array()).trim();
                System.out.println("收到客户端的消息:" + msg);
                // 向其他客户端转发消息
                sendMsgToOtherClients(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了...");
                // 取消注册
                key.cancel();
                // 关闭通道
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {
        }
    }

    /**
     * 转发消息给其他客户端
     *
     * @param msg
     * @param self
     */
    private void sendMsgToOtherClients(String msg, SocketChannel self) {
        System.out.println("服务器转发消息中");
        try {
            for (SelectionKey key : selector.keys()) {
                SelectableChannel targetChannel = key.channel();
                // 排除自己
                if (targetChannel instanceof SocketChannel && targetChannel != self) {
                    SocketChannel channel = (SocketChannel) targetChannel;
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    // 将buffer数据写入channel
                    channel.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.init();
        server.listen();
    }
}
