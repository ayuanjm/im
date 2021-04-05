package com.yuan.im.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/5 4:24 下午
 * @describe:
 */
public class GroupChatClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8088;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    /**
     * 初始化
     */
    private void init() {
        try {
            selector = Selector.open();
            // 连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            // 将socketChannel注册到selector，OP_READ
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 得到userName
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向服务器发送消息
     *
     * @param msg
     */
    private void sendMsg(String msg) {
        msg = userName + "说：" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取从服务器回复的消息
     */
    private void readMsg() {
        try {
            /**
             *  selector.select()是阻塞的，但是防止出现其它线程selector.wakeup()唤醒该Selector，但是又没有事件的情况，
             *  所以才会判断readChannels，而不是简单的认为selector.select()有返回值就一定有事件发生
             */
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        System.out.println(new String(buffer.array()).trim());
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("没有可读的通道...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            client();
        });
        Thread b = new Thread(() -> {
            client();
        });
        a.start();
        b.start();
    }

    private static void client() {
        GroupChatClient client = new GroupChatClient();
        client.init();
        // 线程每隔3秒，读取从服务器发送的数据
        new Thread(() -> {
            while (true) {
                try {
                    client.readMsg();
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            client.sendMsg(scanner.nextLine());
        }
    }
}
