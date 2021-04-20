package com.yuan.im.netty.codec.online.client.one;

import com.yuan.im.netty.codec.online.vo.ImModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @desc:
 * @author: YuanJiaMin
 * @date: 2021/4/6 23:46
 */
public class ProtoNettyClientHandlerA extends SimpleChannelInboundHandler<ImModel.ImDataModel> {

    /**
     * 当通道就绪就会触发该方法
     * 临时：由于通过java代码客户端与服务器建立连接时没有像前端一样传参，所以在这里模拟前端建立连接时传参，from group cmd
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ImModel.ImDataModel message = ImModel.ImDataModel.newBuilder()
                .setCmd(0)
                .setDataType(ImModel.ImDataModel.DataType.SendRequestType)
                .setSendRequest(ImModel.SendRequest.newBuilder()
                        .setFrom(100L)
                        .setGroup("chatA")
                        .build())
                .build();
        ctx.writeAndFlush(message);
    }

    /**
     * 当通道有读取事件时会触发
     *
     * @param ctx
     * @param msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImModel.ImDataModel msg) {
        System.out.println("收到服务器的消息：\n" + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
