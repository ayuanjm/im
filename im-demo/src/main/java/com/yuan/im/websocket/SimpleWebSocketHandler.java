package com.yuan.im.websocket;

import com.dragsun.websocket.client.WebSocketSession;
import com.dragsun.websocket.handler.WebSocketChannelHandlerFactory;
import com.dragsun.websocket.handler.websocket.AbstractWebSocketHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @param
 * @Author: zhuangjiesen
 * @Description: 通用的 netty - websocket 处理器
 * @Date: Created in 2018/5/12
 */
public class SimpleWebSocketHandler extends AbstractWebSocketHandler {

    private static final String SPECIFY_SEND = "sp:";

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketFrame webSocketFrame) {

        if (webSocketFrame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) webSocketFrame;
            String text = textWebSocketFrame.text();

            System.out.println(" 我是接收到的--------- : " + text);
            System.out.println(" thread : " + Thread.currentThread().getName());

            if (text.indexOf(SPECIFY_SEND) == 0) {
                String id = text.substring(SPECIFY_SEND.length(), text.indexOf(";"));
                WebSocketSession socketSession = WebSocketChannelHandlerFactory.webSocketSessions.get(id);
                socketSession.sendMessage(new TextWebSocketFrame(text.substring(text.indexOf(";") + 1)));
            }

            TextWebSocketFrame response = new TextWebSocketFrame("我是返回信息... : " + System.currentTimeMillis());
            webSocketSession.sendMessage(response);


            System.out.println("i am sending ping message ...");
            webSocketSession.sendMessage(new PingWebSocketFrame());
        }

    }

    public static void main(String[] args) {
        String text = "sp:1213;你好";
        String id = text.substring(SPECIFY_SEND.length(), text.indexOf(";"));
        System.out.println(id);
        System.out.println(text.substring(text.indexOf(";") + 1));
    }

}
