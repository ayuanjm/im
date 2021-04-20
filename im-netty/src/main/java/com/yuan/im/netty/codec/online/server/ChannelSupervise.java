package com.yuan.im.netty.codec.online.server;

import com.yuan.im.netty.codec.online.vo.ImModel;
import io.netty.channel.Channel;

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
     * 存储group、from、channel
     */
    private static final Map<String, Map<Long, Channel>> CHANNEL_MAP = new ConcurrentHashMap<>(64);

    /**
     * 注册channel,加入本服务器内存map和redis map
     *
     * @param channel
     */
    public static void addChannel(ImModel.SendRequest request, Channel channel) {
        // 暂时不校验null和并发
        Map<Long, Channel> groupMap = CHANNEL_MAP.getOrDefault(request.getGroup(), new ConcurrentHashMap<>(16));
        groupMap.put(request.getFrom(), channel);
        CHANNEL_MAP.put(request.getGroup(), groupMap);
    }

    /**
     * 移除channel，移除本服务器内存map和redis map
     *
     * @param groupId
     * @param from
     */
    public static void removeChannel(String groupId, Long from) {
        CHANNEL_MAP.getOrDefault(groupId, new ConcurrentHashMap<>(16)).remove(from);
    }

    /**
     * 根据groupId和from查找channel
     *
     * @param groupId
     * @param from
     * @return
     */
    public static Channel findChannel(String groupId, Long from) {
        return CHANNEL_MAP.getOrDefault(groupId, new ConcurrentHashMap<>(16)).get(from);
    }

    /**
     * 向所有人发送消息,会存在我在这个时间点需要发送的人，还没有结束循环时，离线了，或者有新的人加入
     *
     * @param msg
     */
    public static void sendToAll(String groupId, Long from, Object msg) {
        Map<Long, Channel> channelMap = CHANNEL_MAP.getOrDefault(groupId, new ConcurrentHashMap<>(16));
        channelMap.entrySet().parallelStream().forEach(o -> {
            if (!o.getKey().equals(from)) {
                o.getValue().writeAndFlush(msg);
            }
        });
    }

    /**
     * 统计在线人数
     *
     * @return
     */
    public static int onlineSize() {
        return CHANNEL_MAP.values().stream().mapToInt(longChannelMap -> longChannelMap.entrySet().size()).sum();
    }
}
