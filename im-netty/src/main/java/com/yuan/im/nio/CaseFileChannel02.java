package com.yuan.im.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/3 10:41 下午
 * @describe:
 */
public class CaseFileChannel02 {
    public static final String FILE_PATH = "/Users/yuan/eclipse/im/im-netty/src/main/java/com/yuan/im/nio/";

    public static void main(String[] args) throws Exception {
        File file = new File(FILE_PATH + "file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将channel的数据读入到 buffer
        fileChannel.read(byteBuffer);
        // 将byteBuffer的字节数据转成String
        System.out.println(new String(Arrays.copyOf(byteBuffer.array(), byteBuffer.position())));
        fileInputStream.close();
    }
}
