package im.kai.server.service.message.websocket.event.listener;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.MessageSendManager;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import im.kai.server.service.message.websocket.event.SessionConnectedEvent;
import im.kai.server.service.message.websocket.event.SessionMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 消息接收监听事件
 */
@Component
public class SessionMessageReceivedListener {
    @Autowired
    private MessageSendManager messageSendManager;

    /**
     * 将接收到的消息进行转发
     * @param event
     */
    @EventListener
    public void handleConnected(SessionMessageReceivedEvent event){
        WebSocketSessionContext context = (WebSocketSessionContext)event.getSource();
        WebSocketProtos.WebSocketMessage message  = event.getMessage();
        messageSendManager.transferMessageSend(context, message);
    }
}
