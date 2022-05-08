package com.yuan.nio;

import java.nio.ByteBuffer;

/**
 * @author YuanJiaMin
 * @description
 * @date 2022/5/8 22:10
 */

public class BufferPutDemo {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(5);
        System.out.println("capacity:" + buffer.capacity() + " position:" + buffer.position() + " limit:" + buffer.limit());
        byte[] bytes = {1, 2, 3};
        buffer.put(bytes);
        System.out.println("capacity:" + buffer.capacity() + " position:" + buffer.position() + " limit:" + buffer.limit());

        buffer.position(0);
        byte[] dst = new byte[3];
        buffer.get(dst);
        System.out.println("capacity:" + buffer.capacity() + " position:" + buffer.position() + " limit:" + buffer.limit());

        for (byte b : dst) {
            System.out.println(b);
        }
    }
}
