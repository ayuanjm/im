package com.yuan.nio;

import java.nio.ByteBuffer;

/**
 * @author YuanJiaMin
 * @description Buffer
 * @date 2022/5/8 21:36
 */

public class BufferDemo {
    public static void main(String[] args) {
        byte[] data = {'a', 'b', 'c', 'd', 'e'};
        ByteBuffer buffer = ByteBuffer.wrap(data);
        System.out.println(buffer.capacity());
        System.out.println(buffer.limit()); // limit 限制读取的下标，默认等于capacity，容量大小。
        System.out.println(buffer.position());

        System.out.println("-----------------");
        System.out.println((char) buffer.get()); // 读取当前position所在位置的一位数据
        System.out.println(buffer.position()); // position往后移动一位
        buffer.mark(); // 标记当前position的位置。之后可以使用 buffer.reset()，将position的值设置为当时mark时的position值

        System.out.println("-----------------");
        buffer.position(3);
        System.out.println((char) buffer.get()); // 读取当前position所在位置的一位数据
        System.out.println(buffer.position()); // position往后移动一位

        System.out.println("-----------------");
        buffer.reset(); // 重置position的位置，如果之前使用了mark那么会重置到mark时候的position值，否则重置到0
        System.out.println(buffer.position());

        System.out.println("-----------------");
        buffer.limit(4); // 限制读取下标为4的元素
        System.out.println((char) buffer.get()); // 读取当前position所在位置的一位数据
        System.out.println(buffer.position()); // position往后移动一位
    }
}
