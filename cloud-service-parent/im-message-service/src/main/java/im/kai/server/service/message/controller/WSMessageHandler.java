package im.kai.server.service.message.controller;

import im.kai.server.service.message.domain.dto.OfflineMessage;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import im.kai.server.service.message.websocket.WebSocketSessionContextManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Slf4j
public class WSMessageHandler extends BinaryWebSocketHandler {

    @Autowired
    private WebSocketSessionContextManager sessionContextManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionContextManager.onConnected(session);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        WebSocketProtos.WebSocketMessage webSocketMessage;
        try {
            webSocketMessage = WebSocketProtos.WebSocketMessage.parseFrom(message.getPayload());
        }catch (Exception ex){
            WebSocketSessionContext context = sessionContextManager.get(session);
            if(context != null){
                log.error(String.format("websocket会话[%s]接收到错误的消息包", context.getSessionName()), ex);
                context.close();
            }else{
                log.error(String.format("websocket会话[%s(%s)]接收到错误的消息包",
                        session.getId(), session.getRemoteAddress()), ex);
                session.close();
            }
            return;
        }
        sessionContextManager.handleBinaryMessage(session, webSocketMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionContextManager.onClosed(session, status);
    }
}
