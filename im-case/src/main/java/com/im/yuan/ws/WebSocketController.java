package com.im.yuan.ws;

import com.im.yuan.model.ConversationHistoryRecordModel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/8 11:33 上午
 * @describe:
 */
@ServerEndpoint("/websocket")
public class WebSocketController {
    private static final String TO_USER = "toUserId";
    /**
     * 记录连接数量
     * key代表此次客户端的session，value代表此次连接对象。
     */
    private static final Map<String, Session> CONNECT_MAP = new ConcurrentHashMap(16);
    /**
     * 保存所有用户昵称信息
     * key是sessionId，value是用户名
     */
    private static final Map<String, String> USER_MAP = new ConcurrentHashMap(16);
    /**
     * 会话历史记录Demo
     */
    private static final Map<String, List> CONVERSATION_HISTORY_RECORD_MAP = new ConcurrentHashMap(32);

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        String channelId = session.channel().id().asShortText();
        CONNECT_MAP.put(channelId, session);
        System.out.println("sessionId:" + channelId);
        System.out.println("new connection");
        session.sendText("userId：" + channelId);
    }

    @OnClose
    public void onClose(Session session) {
        CONNECT_MAP.remove(session.channel().id().asShortText());
        System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        if (message.indexOf(TO_USER) == 0) {
            String toUserId = message.substring(TO_USER.length() + 1, message.indexOf(";"));
            String fromUserId = session.channel().id().asShortText();
            System.out.println("fromUserId:" + fromUserId + "   toUserId:" + toUserId);
            Session toSession = CONNECT_MAP.get(toUserId);
            message = message.substring(message.indexOf(";") + 1);
            System.out.println(message);
            // 保存会话记录
            saveRecord(message, toUserId, fromUserId);
            // 发送消息给toUser
            toSession.sendText(message);
        } else {
            session.sendText("Hello Netty!");
        }
    }

    /**
     * 保存会话记录
     *
     * @param message
     * @param toUserId
     * @param fromUserId
     */
    private void saveRecord(String message, String toUserId, String fromUserId) {
        ConversationHistoryRecordModel recordModel = new ConversationHistoryRecordModel();
        StringJoiner conversationId = new StringJoiner(":");

        conversationId.add(fromUserId.compareTo(toUserId) > 0 ? toUserId : fromUserId);
        conversationId.add(fromUserId.compareTo(toUserId) < 0 ? toUserId : fromUserId);
        String conversationIdTemp = conversationId.toString();
        recordModel.setConversationId(conversationIdTemp);
        recordModel.setMessage(message);
        recordModel.setCreateTime(new Date());
        //使用数据库时不需要校验存不存在，没有并发覆盖问题
        List recordList = CONVERSATION_HISTORY_RECORD_MAP.getOrDefault(conversationIdTemp, new ArrayList());
        recordList.add(recordModel);
        CONVERSATION_HISTORY_RECORD_MAP.put(conversationIdTemp, recordList);
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

}


