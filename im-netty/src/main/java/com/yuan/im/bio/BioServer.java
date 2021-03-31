package com.yuan.im.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 使用命令连接服务器发送数据
 * telnet 127.0.0.1 8088
 * Ctrl + ]
 * send hello world
 * </p>
 *
 * @desc: BIO通信
 * @author: YuanJiaMin
 * @date: 2021/3/31 21:59
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10, 20, 1,
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(16),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        ServerSocket serverSocket = new ServerSocket(8088);
        System.out.println("server start ...");

        while (true) {
            //监听等待客户端连接
            System.out.println("阻塞等待客户端连接 ...");
            Socket socket = serverSocket.accept();
            System.out.println("one client connect ...");
            threadPoolExecutor.execute(() -> {
                handler(socket);
            });
        }
    }

    /**
     * 和客户端通讯
     *
     * @param socket
     */
    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            InputStream inputStream = socket.getInputStream();
            int length;
            System.out.println("阻塞等待数据read ...");
            while ((length = inputStream.read(bytes)) != -1) {
                System.out.println(Thread.currentThread().getName() + " " + new String(bytes, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("close client connect ...");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
