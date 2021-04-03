package com.yuan.im.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/3 11:12 下午
 * @describe:
 */
public class CaseFileChannel04 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(CaseFileChannel02.FILE_PATH + "file01.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream(CaseFileChannel02.FILE_PATH + "file04.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        // 使用 transferFrom 完成copy
        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());
        inputStreamChannel.close();
        outputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
