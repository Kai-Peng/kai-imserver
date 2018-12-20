package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.WebSocketProtos;

/**
 * 用户的通讯录有变更的通知消息
 */
public class UserContactsChangedNoticeMessage extends UserNoticeMessage {
    public UserContactsChangedNoticeMessage(){
        super(WebSocketProtos.UserNoticeMessage.Type.CONTACTS_CHANGED);
    }

    @Override
    ByteString getNoticeData() {
        return null;
    }
}
