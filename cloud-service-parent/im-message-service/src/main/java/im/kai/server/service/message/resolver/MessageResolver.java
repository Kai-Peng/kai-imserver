package im.kai.server.service.message.resolver;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;

/**
 * 根据消息类型将收到的消息格式转化为发送消息格式
 * @param <F>
 * @param <T>
 */
public interface MessageResolver<F,T> {
    /**
     * 处理的消息类型
     * @return
     */
    WebSocketProtos.WebSocketMessage.Type getType();

    /**
     * 处理消息
     * @param sessionContext
     * @param message
     */
    T resolve(WebSocketSessionContext sessionContext, F message);
}
