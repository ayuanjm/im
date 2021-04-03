package com.yuan.im.nio;

import java.nio.IntBuffer;

/**
 * <p>
 * 1、每个channel都会对应一个Buffer
 * 2、Selector对应一个线程，一个线程对应多个channel（连接）
 * 3、程序切换到哪个channel是由事件决定的，Event是一个重要的概念
 * 4、Selector会根据不同的事件，在各个通道上切换
 * 5、Buffer就是一个内存块，底层是有一个数组
 * 6、数据的读取写入是通过Buffer，这个和BIO不同，BIO要么是输入流，要么是输出流，不能双向
 * 但是NIO的Buffer是可以读也可以写，需要flip方法切换
 * 7、channel是双向的，可以返回底层操作系统的情况，比如Linux底层操作系统通道就是双向的
 * </p>
 * <p>
 * // 标记
 * int mark = -1;
 * // 位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变该值，为下一次读写作准备
 * int position = 0;
 * // 表示缓冲区的当前终点，不能对缓存区超过极限的位置进行读写操作，且极限是可以修改的
 * int limit;
 * // 容量，即可以容纳的最大数据量，在缓冲区创建时被设定并且不能改变
 * int capacity;
 * </p>
 *
 * @desc: 举例说明Buffer的使用
 * @author: YuanJiaMin
 * @date: 2021/3/31 22:57
 */
public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个Buffer，大小为5，即可存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 向Buffer存放数据
        for (int i = 0; i < intBuffer.capacity(); ++i) {
            intBuffer.put(i * 2);
        }
        // 如何从Buffer读取数据，将Buffer转换，读写切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            // 从Buffer中读取数据，每次get后下标自动往后移
            System.out.println(intBuffer.get());
        }
    }
}
