package im.kai.server.service.message.resolver;

import com.netflix.discovery.converters.Auto;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class MessageResolverFactory {
    @Autowired
    MessageResolver sendMessageResolver;

    @Autowired
    MessageResolver offlineMessageResolver;

    @Autowired
    MessageResolver messageNoticeResolver;

    @Autowired
    MessageResolver userNoticeResolver;

    @Autowired
    MessageResolver systemMessageResolver;

    @Nullable
    public MessageResolver getMessageResolver(WebSocketProtos.WebSocketMessage.Type type){
        if(type.equals(WebSocketProtos.WebSocketMessage.Type.SEND_MESSAGE)){
            return sendMessageResolver;
        }else if (type.equals(WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE)){
            return offlineMessageResolver;
        }else if(type.equals(WebSocketProtos.WebSocketMessage.Type.MESSAGE_NOTICE)){
            return messageNoticeResolver;
        }else if(type.equals(WebSocketProtos.WebSocketMessage.Type.USER_NOTICE)){
            return userNoticeResolver;
        } else if (type.equals(WebSocketProtos.WebSocketMessage.Type.SYSTEM_NOTICE)) {
            return systemMessageResolver;
        }
        return null;
    }
}
