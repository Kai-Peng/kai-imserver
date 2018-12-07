package im.kai.server.service.message.websocket.event;

import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.context.ApplicationEvent;

/**
 * 会话已关闭的事件
 */
public class SessionClosedEvent extends ApplicationEvent {
    /**
     *
     * @param context
     */
    public SessionClosedEvent(WebSocketSessionContext context) {
        super(context);
    }
}
