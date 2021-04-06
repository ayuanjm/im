package com.yuan.im.nio.zerocopy;

import com.yuan.im.nio.FileConstant;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author yuan
 */
public class OldIoClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8088);
        InputStream inputStream = new FileInputStream(FileConstant.PDF_PATH);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("发送总字节数： " + total + ", 耗时： " + (System.currentTimeMillis() - startTime));
        inputStream.close();
        dataOutputStream.close();
        socket.close();
    }
}
