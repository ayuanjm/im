package com.yuan.im.nio;

import java.nio.ByteBuffer;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/3 11:17 下午
 * @describe:
 */
public class CaseByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.putInt(3);
        byteBuffer.putLong(4L);
        byteBuffer.putChar('a');

        byteBuffer.flip();

        // 按什么类型放入就需要按什么类型读取，否则会Exception
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());


    }
}
