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
        logger.debug("���ӳɹ�......");
        //���ӳɹ����뷽��
        users.add(session);//�������ӵ��û�����list
    }

    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> webSocketMessage) throws Exception {
        //������Ϣ����
        for (WebSocketSession user : users) {
            if (user.isOpen()) {
                user.sendMessage(new TextMessage(
                                webSocketMessage.getPayload() + "")
                        //������ϢΪTextMessage�����ʽ
                );//�������û�������Ϣ
            }
        }
    }

    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) {
        logger.debug("���ӳ����ر�����......");
        //����
    }

    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) {
        logger.debug("���ӹر�......" + closeStatus.toString());
        //�ر�
    }

    public boolean supportsPartialMessages() {
        return false;
    }
}
