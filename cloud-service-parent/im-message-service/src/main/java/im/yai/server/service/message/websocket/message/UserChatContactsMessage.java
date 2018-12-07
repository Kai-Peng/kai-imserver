package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

import java.io.UnsupportedEncodingException;

/*
名片消息定义
 */
public class UserChatContactsMessage extends UserChatMessage {
    /**
     * 类型
     */
    private MessageProtos.ContactsMessage.Type type;
    /**
     * id（用户则为userId, 群则为群id)
     */
    private String id;
    /**
     * 昵称
     */
    private String name;
    /**
     *头像
     */
    private String avatar;

    @Override
    ByteString getContent() {
        MessageProtos.ContactsMessage.Builder builder = MessageProtos.ContactsMessage.newBuilder();
        try {
            builder.setType(this.type);
            builder.setId(this.id);
            builder.setName(ByteString.copyFrom(this.name,"UTF-8"));
            builder.setAvatar(this.avatar);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return builder.build().toByteString();
    }
}
