package com.yuan.im.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <p>
 * Scattering：将数据写入buffer时，可以采用buffer数组依次写入 【分散】
 * Gathering：从buffer读取数据时，可以采用buffer数组依次读
 * </P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/3 11:55 下午
 * @describe:
 */
public class CaseScatterAndGather {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
        // 绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        // 等待客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        int msgLength = 8;
        while (true) {
            int readCount = 0;
            while (readCount < msgLength) {
                // 将channel的数据读入buffer
                long l = socketChannel.read(byteBuffers);
                readCount += l;
                System.out.println("readCount:" + readCount);
                Arrays.stream(byteBuffers)
                        .map(byteBuffer -> "position:" + byteBuffer.position() + " ,limit:" + byteBuffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.stream(byteBuffers).forEach(buffer -> buffer.flip());

            int writeCount = 0;

            while (writeCount < msgLength) {
                //将byteBuffers数据读出到channel显示到客户端
                long l = socketChannel.write(byteBuffers);
                writeCount += l;
            }

            Arrays.stream(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("readCount:" + readCount + ",writeCount:" + writeCount + ",msgLength:" + msgLength);
        }
    }
}
