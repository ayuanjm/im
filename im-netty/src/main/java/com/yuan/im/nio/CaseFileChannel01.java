package com.yuan.im.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <p>
 * // 将channel的数据读入到buffer
 * channel.read(buffer)
 * // 将buffer的数据写入channel
 * channel.write(buffer)
 * </P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/3 10:19 下午
 * @describe:
 */
public class CaseFileChannel01 {
    public static final String FILE_PATH = "/Users/yuan/eclipse/im/im-netty/src/main/java/com/yuan/im/nio/file01.txt";

    public static void main(String[] args) throws Exception {

        String msg = "hello world 中文";
        // 创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH);
        // 通过fileOutputStream 获取对应的 fileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将msg放入byteBuffer
        byteBuffer.put(msg.getBytes(StandardCharsets.UTF_8));
        // 切换为读模式
        byteBuffer.flip();
        // 将byteBuffer数据写入fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
