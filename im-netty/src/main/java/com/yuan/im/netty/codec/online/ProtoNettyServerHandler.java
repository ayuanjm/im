package com.yuan.im.netty.codec.online;

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
public class ProtoNettyServerHandler extends SimpleChannelInboundHandler<ImDataInfo.MyMessage> {
    /**
     * 读取数据实现（这里我们可以读取客户端发送的消息）
     *
     * @param channelHandlerContext 上下文对象，含有 管道pipeline、通道channel、地址
     * @param msg                   客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ImDataInfo.MyMessage msg) throws Exception {
        // 根据dataType 来显示不同的信息
        ImDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == ImDataInfo.MyMessage.DataType.UserType) {
            ImDataInfo.User student = msg.getUser();
            System.out.println("学生id=" + student.getId() + " 学生名字=" + student.getName());
        } else if (dataType == ImDataInfo.MyMessage.DataType.WorkerType) {
            ImDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人的名字=" + worker.getName() + " 年龄=" + worker.getAge());
        } else {
            System.out.println("传输的类型不正确");
        }
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
