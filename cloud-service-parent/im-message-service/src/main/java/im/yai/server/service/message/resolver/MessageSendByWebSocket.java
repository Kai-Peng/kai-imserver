package im.kai.server.service.message.resolver;

import im.kai.server.exception.ServerErrorException;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.validator.ValidatorChainFactory;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;

public class MessageSendByWebSocket{
    /**
     * 通过WebSocket发送消息
     * @param toWsCtx 接收方session上下文
     * @param toMsg 消息体
     */
    public void messageSend(WebSocketSessionContext toWsCtx, byte[] toMsg) {
        try {
            toWsCtx.getSession().sendMessage(new BinaryMessage(toMsg));
        } catch (IOException e) {
            throw new ServerErrorException("发送消息失败", e);
        }
    }
}
