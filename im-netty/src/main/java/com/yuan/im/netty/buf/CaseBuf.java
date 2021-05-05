package com.yuan.im.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author YuanJiaMin
 * @date 2021/5/5 12:49 下午
 * @description
 */
public class CaseBuf {
    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
        byteBuf.writeBytes(new byte[]{1,2,3});
        byteBuf.release();
        byteBuf.writeBytes(new byte[]{1,2,3});
    }
}
