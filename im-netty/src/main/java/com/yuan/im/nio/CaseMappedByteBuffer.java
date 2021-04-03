package com.yuan.im.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <p>
 * 1、MappedByteBuffer 可以让文件直接在内存(堆外内存)中修改，操作系统不需要拷贝一次
 * </P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/3 11:37 下午
 * @describe:
 */
public class CaseMappedByteBuffer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(CaseFileChannel01.FILE_PATH, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * // FileChannel.MapMode.READ_WRITE 使用的读写模式
         * MapMode mode
         * // 0：可以直接修改的起始位置
         * long position
         * // 5：映射到内存的大小，即将file01.txt的多少个字节映射到内存
         * long size
         * 可以直接修改的范围就是内部数组下标0-4
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'a');
        mappedByteBuffer.put(4, (byte) 1);
        randomAccessFile.close();

    }
}
