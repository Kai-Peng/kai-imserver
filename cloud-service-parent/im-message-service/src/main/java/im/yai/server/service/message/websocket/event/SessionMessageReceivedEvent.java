package im.kai.server.service.message.websocket.event;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.context.ApplicationEvent;

/**
 * 会话收到消息的事件
 */
public class SessionMessageReceivedEvent extends ApplicationEvent {

    private WebSocketProtos.WebSocketMessage message;

    /**
     *
     * @param context
     */
    public SessionMessageReceivedEvent(WebSocketSessionContext context,
                                       WebSocketProtos.WebSocketMessage message) {
        super(context);
        this.message = message;
    }

    /**
     * 返回收到的消息
     * @return
     */
    public WebSocketProtos.WebSocketMessage getMessage() {
        return message;
    }

}
