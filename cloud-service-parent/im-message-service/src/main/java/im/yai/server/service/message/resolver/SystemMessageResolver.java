package im.kai.server.service.message.resolver;

import im.kai.server.service.message.protocol.MessageProtos;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import im.kai.server.service.message.websocket.message.AppVersionMessage;
import im.kai.server.service.message.websocket.message.UserOfflineMessage;
import org.springframework.stereotype.Component;

/**
 * 消息发送
 */
@Component
public class SystemMessageResolver implements MessageResolver<MessageProtos.Message,byte[]> {
    @Override
    public WebSocketProtos.WebSocketMessage.Type getType() {
        return WebSocketProtos.WebSocketMessage.Type.SYSTEM_NOTICE;
    }

    @Override
    public byte[] resolve(WebSocketSessionContext sessionContext, MessageProtos.Message message) {
        //推送App版本消息给客户端
        AppVersionMessage appVersionMessage = new AppVersionMessage();
        appVersionMessage.setVersion("1.0.0");
        appVersionMessage.setUpdateLog("我有更新数据,yes ok!");
        appVersionMessage.setUpdateUrl("http://www.yai.im/app/release.apk");
        appVersionMessage.setDownloadMode(WebSocketProtos.SystemNoticeMessage.AppVersionMessage.Mode.DOWNLOAD_MODE_INSTALL);
        appVersionMessage.setUpdateMode(WebSocketProtos.SystemNoticeMessage.AppVersionMessage.Mode.UPDATE_MODE_MANUAL);

        return appVersionMessage.buildBytesMsg();
    }
}
