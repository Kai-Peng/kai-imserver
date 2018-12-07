package im.kai.server.service.message.resolver;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class MessageResolverFactory {
    @Nullable
    public static MessageResolver getMessageResolver(WebSocketProtos.WebSocketMessage.Type type){
        if(type.equals(WebSocketProtos.WebSocketMessage.Type.SEND_MESSAGE)){
            return new SendMessageResolver();
        }else if (type.equals(WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE)){
            return new OfflineMessageResolver();
        }else if(type.equals(WebSocketProtos.WebSocketMessage.Type.MESSAGE_NOTICE)){
            return new MessageNoticeResolver();
        }else if(type.equals(WebSocketProtos.WebSocketMessage.Type.USER_NOTICE)){
            return new UserNoticeResolver();
        } else if (type.equals(WebSocketProtos.WebSocketMessage.Type.SYSTEM_NOTICE)) {
            return new SystemMessageResolver();
        }
        return null;
    }
}
