package com.yuan.im.netty.simple;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 1、我们自定义一个Handler，需要继承Netty规定好的某个HandlerAdapter
 * 2、这时我们自定义的Handler，才能称为一个Handler
 * </p>
 *
 * @desc: NettyServerHandler
 * @author: YuanJiaMin
 * @date: 2021/4/6 23:16
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据实现（这里我们可以读取客户端发送的消息）
     *
     * @param ctx 上下文对象，含有 管道pipeline、通道channel、地址
     * @param msg 客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        readMsg(ctx, (ByteBuf) msg);
        /**
         * 如果有一个非常耗时的任务 -> 异步执行 -> 提交该channel对应的NioEventLoop的taskQueue中
         * ctx.channel().eventLoop()，相当于线程池，将任务提交到线程池中异步执行
         */
        // 解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("execute hello world 客户端", CharsetUtil.UTF_8));
        });

        // 解决方案2  用户自定义定时任务 该任务提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(() -> {
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("schedule hello world 客户端", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);
        System.out.println("go on ...");
    }

    private void readMsg(ChannelHandlerContext ctx, ByteBuf msg) {
        System.out.println("服务器读取线程:" + Thread.currentThread().getName());
        System.out.println("server ctx:" + JSON.toJSONString(ctx));
        System.out.println("查看channel和pipeline的关系");
        Channel channel = ctx.channel();
        // 本质是一个双向链表
        ChannelPipeline pipeline = ctx.pipeline();
        // 将msg转成一个ByteBuf，ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf byteBuf = msg;
        System.out.println("客户端发送的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**
         * writeAndFlush是write+flush
         * 将数据写入缓存，并刷新
         * 对发送的数据进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello world 客户端", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
