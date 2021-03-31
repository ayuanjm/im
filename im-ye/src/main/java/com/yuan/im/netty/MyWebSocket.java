package com.yuan.im.netty;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/3/16 3:16 下午
 */
@ServerEndpoint
public class MyWebSocket {

    private static final String TO_USER = "sessionId";
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

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        String channelId = session.channel().id().asLongText();
        CONNECT_MAP.put(channelId, session);
        System.out.println("sessionId:" + channelId);
        System.out.println("new connection");
        session.sendText("sessionId:" + channelId);
    }

    @OnClose
    public void onClose(Session session) {
        CONNECT_MAP.remove(session.channel().id().asLongText());
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
            String channelId = message.substring(TO_USER.length() + 1, message.indexOf(";"));
            System.out.println("toUser:" + channelId);
            Session toSession = CONNECT_MAP.get(channelId);
            message = message.substring(message.indexOf(";") + 1);
            System.out.println(message);
            toSession.sendText(message);
        } else {
            session.sendText("Hello Netty!");
        }
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

