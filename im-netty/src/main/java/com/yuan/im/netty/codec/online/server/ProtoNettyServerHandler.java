package com.yuan.im.netty.codec.online.server;

import com.yuan.im.netty.codec.online.vo.ImRequest;
import com.yuan.im.netty.codec.online.vo.ImResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
public class ProtoNettyServerHandler extends SimpleChannelInboundHandler<ImRequest.ImRequestInfo> {
    /**
     * 读取数据实现（这里我们可以读取客户端发送的消息）
     *
     * @param channelHandlerContext 上下文对象，含有 管道pipeline、通道channel、地址
     * @param msg                   客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ImRequest.ImRequestInfo msg) throws Exception {
        // 根据dataType 来显示不同的信息
        ImRequest.ImRequestInfo.DataType dataType = msg.getDataType();
        if (dataType == ImRequest.ImRequestInfo.DataType.SendRequestType) {
            ImRequest.SendRequest sendRequest = msg.getSendRequest();
            System.out.println("接收到客户端的sendRequest请求：" + sendRequest.toString());
        } else if (dataType == ImRequest.ImRequestInfo.DataType.PullRequestType) {
            ImRequest.PullRequest pullRequest = msg.getPullRequest();
            System.out.println("接收到客户端的pullRequest请求：" + pullRequest.toString());
            ImResponse.ImResponseInfo responseInfo = ImResponse.ImResponseInfo.newBuilder().setDataType(ImResponse.ImResponseInfo.DataType.PullResponseType)
                    .setPullResponse(ImResponse.PullResponse.newBuilder()
                            .setContent("hello world")
                            .setMsgId(100L)
                            .setGroup("ab")
                            .setFrom(1L)
                            .setSendTime(System.currentTimeMillis())
                            .setCmdId(3)
                            .build()).build();
            channelHandlerContext.writeAndFlush(responseInfo);
        } else {
            System.out.println("传输的类型不正确");
        }
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
