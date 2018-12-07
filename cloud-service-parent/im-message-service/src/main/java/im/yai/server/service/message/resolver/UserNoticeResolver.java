package im.kai.server.service.message.resolver;

import com.google.protobuf.InvalidProtocolBufferException;
import im.kai.server.domain.websocket.WebSocketAuthToken;
import im.kai.server.exception.ServerErrorException;
import im.kai.server.service.message.protocol.MessageProtos;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * 7 = 与消息相关的通知包
 */
@Component
public class UserNoticeResolver implements MessageResolver<WebSocketProtos.WebSocketMessage ,byte[]> {
    @Override
    public WebSocketProtos.WebSocketMessage.Type getType() {
        return WebSocketProtos.WebSocketMessage.Type.USER_NOTICE;
    }

    private WebSocketProtos.UserNoticeMessage.Type userNoticeMsgType;

    public WebSocketProtos.UserNoticeMessage.Type getUserNoticeMsgType() {
        return userNoticeMsgType;
    }

    public void setUserNoticeMsgType(WebSocketProtos.UserNoticeMessage.Type userNoticeMsgType) {
        this.userNoticeMsgType = userNoticeMsgType;
    }

    @Override
    public byte[] resolve(WebSocketSessionContext sessionContext, WebSocketProtos.WebSocketMessage message) {
        WebSocketProtos.WebSocketMessage.Builder builder = WebSocketProtos.WebSocketMessage.newBuilder();
        builder.setType(WebSocketProtos.WebSocketMessage.Type.USER_NOTICE);
        WebSocketProtos.UserNoticeMessage.Builder builder1 = WebSocketProtos.UserNoticeMessage.newBuilder();
        builder1.setType(this.userNoticeMsgType);
        builder.setData(builder1.build().toByteString());
        return builder.build().toByteArray();
    }
}
