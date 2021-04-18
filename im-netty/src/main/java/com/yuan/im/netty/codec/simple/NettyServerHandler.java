package com.yuan.im.netty.codec.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

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
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {
    /**
     * 读取数据实现（这里我们可以读取客户端发送的消息）
     *
     * @param channelHandlerContext 上下文对象，含有 管道pipeline、通道channel、地址
     * @param msg                   客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StudentPOJO.Student msg) throws Exception {
        System.out.println("客户端发送的数据 id=" + msg.getId() + " 名字=" + msg.getName());
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
