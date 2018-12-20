package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 用户文本聊天消息定义
 */
public class UserChatTextMessage extends UserChatMessage {
    /**
     * 文本聊天信息
     */
    private String content;

    @Override
    ByteString getContent() {
        ByteString byteString = null;
        if (this.content!=null){
            try {
                byteString = ByteString.copyFrom(this.content,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return byteString;
    }
}
