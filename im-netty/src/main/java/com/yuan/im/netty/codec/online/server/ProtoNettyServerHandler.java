package com.yuan.im.netty.codec.online.server;

import com.yuan.im.netty.codec.online.constant.ImCmd;
import com.yuan.im.netty.codec.online.vo.ImModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * <p>
 * 1、我们自定义一个Handler，需要继承Netty规定好的某个HandlerAdapter
 * 2、这时我们自定义的Handler，才能称为一个Handler
 * 3、每一个客户端连接都会对应一个ProtoNettyServerHandler对象，该对象多例，类似springboot中的ServerEndpoint注解的类
 * </p>
 *
 * @desc: NettyServerHandler
 * @author: YuanJiaMin
 * @date: 2021/4/6 23:16
 */
public class ProtoNettyServerHandler extends SimpleChannelInboundHandler<ImModel.ImDataModel> {
    /**
     * 建立连接时的group会话id
     */
    private String group;
    /**
     * 建立连接时的from用户id
     */
    private Long from;

    /**
     * 业务逻辑链准备完毕，链接激活
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端加入连接:" + ctx.channel().hashCode());
        System.out.println("ProtoNettyServerHandler:" + hashCode());
    }

    /**
     * 失去连接
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ChannelSupervise.removeChannel(group, from);
        System.out.println("客户端断开连接:" + ctx.channel().hashCode() + "  在线人数:" + ChannelSupervise.onlineSize());
        System.out.println("ProtoNettyServerHandler:" + hashCode());
    }

    /**
     * 读取数据实现（这里我们可以读取客户端发送的消息）
     *
     * @param ctx 上下文对象，含有 管道pipeline、通道channel、地址
     * @param msg 客户端发送的数据，默认Object
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImModel.ImDataModel msg) {
        // 临时：建立连接,正常情况不会在这出现0
        if (msg.getCmd() == ImCmd.CONNECT) {
            ImModel.SendRequest sendRequest = msg.getSendRequest();
            this.from = sendRequest.getFrom();
            this.group = sendRequest.getGroup();
            ChannelSupervise.addChannel(sendRequest, ctx.channel());
            System.out.println("接收到客户端建立连接的请求：\n" + sendRequest.getContent());
            return;
        }
        // 发送消息
        if (msg.getCmd() == ImCmd.SEND_MSG) {
            sendMsg(ctx, msg);
            return;
        }
        // 心跳检测
        if (msg.getCmd() == ImCmd.HEART_BEAT) {
            System.out.println("客户端的心跳ping请求:\n" + ctx.hashCode());
        }
    }

    private void sendMsg(ChannelHandlerContext ctx, ImModel.ImDataModel msg) {
        // 根据dataType 来显示不同的信息
        ImModel.ImDataModel.DataType dataType = msg.getDataType();
        System.out.println("客户端消息:\n"+msg);
        if (dataType == ImModel.ImDataModel.DataType.SendRequestType) {
            // 发送消息到群组
            ChannelSupervise.sendToAll(group, from, msg);
        } else if (dataType == ImModel.ImDataModel.DataType.PullRequestType) {
            ImModel.PullRequest pullRequest = msg.getPullRequest();
            System.out.println("接收到客户端的pullRequest请求：\n" + pullRequest.toString());
            ImModel.ImDataModel responseInfo = ImModel.ImDataModel.newBuilder()
                    .setDataType(ImModel.ImDataModel.DataType.PullResponseType)
                    .setPullResponse(ImModel.PullResponse.newBuilder()
                            .addMsg(ImModel.PullMsg.newBuilder()
                                    .setContent("hello world")
                                    .setMsgId(100L)
                                    .setGroup("ab")
                                    .setFrom(1L)
                                    .setSendTime(System.currentTimeMillis())
                                    .setCmdId(3)
                                    .build()).build()).build();
            ctx.writeAndFlush(responseInfo);
        }
    }

    /**
     * 处理异常
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        ChannelSupervise.removeChannel(group, from);
    }

    /**
     * 心跳检测
     *
     * @param ctx 上下文
     * @param evt 事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--超时时间--" + eventType);
            // 如果发生空闲，我们关闭通道
            ctx.channel().close();
            ChannelSupervise.removeChannel(group, from);
        }
    }
}
