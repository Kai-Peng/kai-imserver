package im.kai.server.service.message.websocket.event;

import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.context.ApplicationEvent;

/**
 * 会话已连接的事件
 */
public class SessionConnectedEvent extends ApplicationEvent {
    /**
     *
     * @param context
     */
    public SessionConnectedEvent(WebSocketSessionContext context) {
        super(context);
    }
}
