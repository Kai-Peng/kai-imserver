package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

import java.io.UnsupportedEncodingException;

/*
位置消息定义
 */
public class UserChatLocationMessage extends UserChatMessage {
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 位置名称
     */
    private String title;
    /**
     * 截图地址
     */
    private String url;

    @Override
    ByteString getContent() {
        MessageProtos.LocationMessage.Builder builder = MessageProtos.LocationMessage.newBuilder();
        try {
            builder.setLongitude(this.longitude);
            builder.setLatitude(this.latitude);
            builder.setTitle(ByteString.copyFrom(this.title,"UTF-8"));
            builder.setUrl(this.url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        return builder.build().toByteString();
    }
}
