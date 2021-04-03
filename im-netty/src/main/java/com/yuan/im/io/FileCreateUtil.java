package com.yuan.im.io;

import java.io.*;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/2 11:11 上午
 * @describe:
 */
public class FileCreateUtil {
    public static void main(String[] args) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            FileInputStream inputStream = new FileInputStream("/Users/yuan/eclipse/im/im-netty/src/main/java/com/yuan/im/io/notice");
            bufferedInputStream = new BufferedInputStream(inputStream);
            File file = new File("/Users/yuan/eclipse/im/im-netty/src/main/java/com/yuan/im/io/yuan.txt");
            FileOutputStream outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            int length;
            byte[] bytes = new byte[1024];
            while ((length = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, length);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
