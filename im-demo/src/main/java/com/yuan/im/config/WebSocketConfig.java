package com.yuan.im.config;

import com.dragsun.websocket.server.WebSocketNettyServer;
import com.yuan.im.websocket.SimpleWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/3/29 7:15 下午
 * @describe:
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public SimpleWebSocketHandler simpleWebSocketHandler() {
        return new SimpleWebSocketHandler();
    }

    @Bean
    public com.dragsun.websocket.server.Configuration configuration() {
        com.dragsun.websocket.server.Configuration configuration = new com.dragsun.websocket.server.Configuration();
        configuration.setWebSocketHandler(simpleWebSocketHandler());
        return configuration;
    }

    @Bean
    public WebSocketNettyServer webSocketNettyServer() {
        WebSocketNettyServer webSocketNettyServer = new WebSocketNettyServer();
        webSocketNettyServer.setPort(80);
        webSocketNettyServer.setConfiguration(configuration());
        return webSocketNettyServer;
    }
}
