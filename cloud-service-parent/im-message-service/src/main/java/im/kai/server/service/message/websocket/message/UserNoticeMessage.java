package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.WebSocketProtos;

/**
 * 与用户相关的通知消息
 */
public abstract class UserNoticeMessage extends WebSocketMessage {

    private WebSocketProtos.UserNoticeMessage.Type type;

    /**
     * 根据通知类型实例化数据
     * @param type
     */
    public UserNoticeMessage(WebSocketProtos.UserNoticeMessage.Type type){
        super(WebSocketProtos.WebSocketMessage.Type.USER_NOTICE);
        this.type = type;
    }

    /**
     * 获取通知数据
     * @return
     */
    abstract ByteString getNoticeData();

    @Override
    ByteString getData() {
        WebSocketProtos.UserNoticeMessage.Builder builder = WebSocketProtos.UserNoticeMessage.newBuilder();
        builder.setType(this.type);
        ByteString noticeData = this.getNoticeData();
        if(noticeData != null) builder.setData(noticeData);
        return builder.build().toByteString();
    }
}
