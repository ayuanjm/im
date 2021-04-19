package com.yuan.im.netty.codec.online;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 * @desc:
 * @author: YuanJiaMin
 * @date: 2021/4/6 23:46
 */
public class ProtoNettyClientHandler extends SimpleChannelInboundHandler<ImResponse.ImResponseInfo> {
    /**
     * 当通道就绪就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送User或者Worker对象
        int nextInt = new Random().nextInt(3);
        ImDataInfo.MyMessage message = null;
        if (0 == nextInt) {
            message = ImDataInfo.MyMessage.newBuilder().setDataType(ImDataInfo.MyMessage.DataType.UserType)
                    .setUser(ImDataInfo.User.newBuilder().setId(18).setName("张三").build()).build();
        } else {
            message = ImDataInfo.MyMessage.newBuilder().setDataType(ImDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(ImDataInfo.Worker.newBuilder().setAge(20).setName("李四").build()).build();
        }
        ctx.writeAndFlush(message);
    }

    /**
     * 当通道有读取事件时会触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImResponse.ImResponseInfo msg) throws Exception {
        ImResponse.PullResponse pullResponse = msg.getPullResponse();
        System.out.println(pullResponse.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
