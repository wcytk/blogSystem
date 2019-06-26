package com.wcytk.service.impl;

import com.wcytk.entity.User;
import com.wcytk.websocket.UserHandler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class MyWebSocketHandler implements WebSocketHandler {
    private Logger logger = Logger.getLogger(MyWebSocketHandler.class);

    private UserHandler userHandler;

    private static final ArrayList<WebSocketSession> users =
            new ArrayList<>();

    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        logger.debug("连接成功......");
        //链接成功进入方法
        users.add(session);//把新链接的用户加入list
    }

    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> webSocketMessage) throws Exception {
        //接受消息方法
        for (WebSocketSession user : users) {
            if (user.isOpen()) {
                user.sendMessage(new TextMessage(
                                webSocketMessage.getPayload() + "")
                        //发送消息为TextMessage对象格式
                );//给在线用户发送消息
            }
        }
    }

    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) {
        logger.debug("链接出错，关闭链接......");
        //出错
    }

    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) {
        logger.debug("链接关闭......" + closeStatus.toString());
        //关闭
    }

    public boolean supportsPartialMessages() {
        return false;
    }
}
