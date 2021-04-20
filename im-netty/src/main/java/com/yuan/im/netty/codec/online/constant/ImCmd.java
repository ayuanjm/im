package com.yuan.im.netty.codec.online.constant;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/20 2:39 下午
 * @describe: 0建立连接，1发送消息，2心跳检测，3ack
 */
public class ImCmd {
    public static int CONNECT = 0;
    public static int SEND_MSG = 1;
    public static int HEART_BEAT = 2;
}
