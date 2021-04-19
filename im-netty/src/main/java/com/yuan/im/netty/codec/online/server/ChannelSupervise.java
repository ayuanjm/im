//package com.yuan.im.netty.codec.online;
//
//import com.yuan.im.netty.codec.online.vo.ImRequest;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelId;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
// * <P></P>
// *
// * @author: YuanJiaMin
// * @date: 2021/4/1 2:33 下午
// * @describe: 保存客户端信息
// */
//public class ChannelSupervise {
//    /**
//     * 存储group、from、channel
//     */
//    private static final Map<String, Map<Long, ChannelId>> CHANNEL_MAP = new ConcurrentHashMap<>(64);
//
//    /**
//     * 注册channel
//     *
//     * @param channel
//     */
//    public static void addChannel(ImRequest.ImRequestInfo requestInfo, Channel channel) {
//        ImRequest.SendRequest request = requestInfo.getSendRequest();
//        // 暂时不校验null和并发
//        Map<Long, ChannelId> groupMap = CHANNEL_MAP.getOrDefault(request.getGroup(), new ConcurrentHashMap<>(16));
//        groupMap.put(request.getFrom(), channel.id());
//        CHANNEL_MAP.put(request.getGroup(), groupMap);
//    }
//
//    /**
//     * 移除channel
//     *
//     * @param channel
//     */
//    public static void removeChannel(Channel channel) {
//        CHANNEL_MAP.remove(channel.id().asShortText());
//    }
//
//    /**
//     * 根据id查找channel
//     *
//     * @param id
//     * @return
//     */
//    public static Channel findChannel(String id) {
//        return GLOBAL_GROUP.find(CHANNEL_MAP.get(id));
//    }
//
//    /**
//     * 向所有人发送消息
//     *
//     * @param msg
//     */
//    public static void sendToAll(Object msg) {
//        GLOBAL_GROUP.writeAndFlush(msg);
//    }
//}
