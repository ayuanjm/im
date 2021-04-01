package com.yuan.im.netty.ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/1 2:33 下午
 * @describe: 保存客户端信息
 */
public class ChannelSupervise {
    /**
     * 存储channel
     */
    private static final ChannelGroup GLOBAL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 存储id与channelId的对应关系
     */
    private static final Map<String, ChannelId> CHANNEL_MAP = new ConcurrentHashMap<>(16);

    /**
     * 注册channel
     *
     * @param channel
     */
    public static void addChannel(Channel channel) {
        GLOBAL_GROUP.add(channel);
        CHANNEL_MAP.put(channel.id().asShortText(), channel.id());
    }

    /**
     * 移除channel
     *
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        GLOBAL_GROUP.remove(channel);
        CHANNEL_MAP.remove(channel.id().asShortText());
    }

    /**
     * 根据id查找channel
     *
     * @param id
     * @return
     */
    public static Channel findChannel(String id) {
        return GLOBAL_GROUP.find(CHANNEL_MAP.get(id));
    }

    /**
     * 向所有人发送消息
     *
     * @param tws
     */
    public static void sendToAll(TextWebSocketFrame tws) {
        GLOBAL_GROUP.writeAndFlush(tws);
    }
}
