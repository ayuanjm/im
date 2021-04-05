package com.yuan.im.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <p>
 * // 阻塞，但是可以被selector.wakeup()唤醒；selector.select()有返回值不是一定有事件发生
 * selector.select()
 * // 阻塞 1000 毫秒,在 1000 毫秒后返回
 * selector.select(1000);
 * // 唤醒 selector
 * selector.wakeup();
 * // 不阻塞,立马返还
 * selector.selectNow();
 * </P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/4 10:53 下午
 * @describe:
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到一个Selector对象
        Selector selector = Selector.open();
        // 绑定一个端口8088，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(8088));
        // 将serverSocketChannel设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel注册到selector，监听事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("注册后的SelectionKey数量：" + selector.keys().size());
        // 循环等待客户端连接
        while (true) {
            // 等待1秒，如果没有事件发生，返回0；如果有事件发生立刻返回事件数量
            if (selector.select(3000) == 0) {
                System.out.println("服务器等待了1秒，无连接！");
                continue;
            }
            // 如果有事件发生，selector.select返回的>0，就获取到相关的SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 如果是OP_ACCEPT，有新的客户端连接
                if (selectionKey.isAcceptable()) {
                    // 为该客户端生成一个socketChannel；该socketChannel和NioClient的socketChannel不是同一个，他们的ByteBuffer也不是同一个。
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel:" + socketChannel.hashCode());
                    // 将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将socketChannel注册到selector，监听事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2048));
                    System.out.println("客户端连接后，注册的SelectionKey数量：" + selector.keys().size());
                }
                // 如果是OP_READ，有客户端发送数据
                if (selectionKey.isReadable()) {
                    // 通过selectionKey反向获取channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    // 将channel中的数据读入buffer
                    socketChannel.read(byteBuffer);
                    System.out.println("收到客户端消息：" + new String(Arrays.copyOf(byteBuffer.array(), byteBuffer.position())));
                }

                /**
                 * 手动从集合中移除当前的selectionKey，防止重复操作
                 * 在每次迭代时, 我们都调用 keyIterator.remove() 代码块，将这个 key 从迭代器中删除。
                 * 因为 #select() 方法仅仅是简单地将就绪的 Channel 对应的 SelectionKey 放到 selected keys 集合中。
                 * 因此，如果我们从 selected keys 集合中，获取到一个 key ，但是没有将它删除，那么下一次 #select 时, 这个 SelectionKey 还在 selectedKeys 中.
                 */
                iterator.remove();
            }
        }
    }
}
