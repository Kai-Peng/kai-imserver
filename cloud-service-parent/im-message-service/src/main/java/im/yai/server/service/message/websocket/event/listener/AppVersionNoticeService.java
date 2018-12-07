package im.kai.server.service.message.websocket.event.listener;

import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.MessageSendManager;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import im.kai.server.service.message.websocket.event.SessionConnectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * APP版本更新通知服务
 */
@Component
public class AppVersionNoticeService {
    @Autowired
    private MessageSendManager messageSendManager;

    /**
     * 处理会话注册信息
     * @param event
     */
    @EventListener
    public void handleConnected(SessionConnectedEvent event){
        WebSocketSessionContext context = (WebSocketSessionContext)event.getSource();

        String version  = "1.0.1";

        if(version.equals(context.getAuthToken().getDevice().getAppVersion())){
            //版本一样则不需要处理
            return;
        }

        //推送App版本消息给客户端
        messageSendManager.noticeMsgSend(context, WebSocketProtos.WebSocketMessage.Type.SYSTEM_NOTICE);
    }
}
