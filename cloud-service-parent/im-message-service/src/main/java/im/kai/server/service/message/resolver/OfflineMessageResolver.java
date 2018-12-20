package im.kai.server.service.message.resolver;

import im.kai.server.service.message.protocol.MessageProtos;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import im.kai.server.service.message.websocket.message.UserOfflineMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 消息发送
 */
@Component
public class OfflineMessageResolver implements MessageResolver<MessageProtos.Message,byte[]> {
    @Override
    public WebSocketProtos.WebSocketMessage.Type getType() {
        return WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE;
    }

    @Override
    public byte[] resolve(WebSocketSessionContext sessionContext,  MessageProtos.Message message) {
        //组装下线消息
        UserOfflineMessage offlineMessage = new UserOfflineMessage(WebSocketProtos.UserOfflineMessage.Reason.DEVICE);
        offlineMessage.setDeivceName(sessionContext.getAuthToken().getDevice().getDeviceSubType().getName());
        offlineMessage.setRemoteAddress(sessionContext.getSession().getRemoteAddress().getHostString());

        return offlineMessage.buildBytesMsg();
    }
}
