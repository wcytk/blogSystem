package com.wcytk.websocket;

import com.wcytk.service.impl.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket // ¿ªÆôwebsocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private MyWebSocketHandler webSocketHandler;
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/echo").setAllowedOrigins("*");
    }
}
