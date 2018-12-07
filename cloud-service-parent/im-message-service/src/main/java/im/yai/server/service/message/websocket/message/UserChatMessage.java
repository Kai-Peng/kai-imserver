package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import im.kai.server.service.message.protocol.MessageProtos;
import im.kai.server.service.message.protocol.WebSocketProtos;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户聊天消息抽象定义类
 */
public abstract class UserChatMessage extends WebSocketMessage {
    /**
     * 消息序号
     */
    private long seq;
    /**
     * 聊天消息类型
     * 1 = 普通文本消息，
     * 2 = 图片消息
     * 3 = 音频消息
     * 4 = 视频消息
     * 5 = 文件消息
     * 6 = 名片消息
     * 7 = 位置消息
     * 21 = 语音通话
     * 22 = 视频通话
     */
    private MessageProtos.Message.Type type;
    /**
     * 群id
     */
    private String toRoomId;
    /**
     * 用户id
     */
    private String toUserId;
    /**
     * 设备ID
     */
    private int toDeviceType;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 获取数据
     * @return
     */
    abstract ByteString getContent();

    /**
     * 实例化聊天消息
     */
    UserChatMessage(){
        super(WebSocketProtos.WebSocketMessage.Type.SEND_MESSAGE);
    }

    @Override
    ByteString getData() {
        MessageProtos.Message.Builder builder = MessageProtos.Message.newBuilder();
        builder.setSeq(this.seq);
        builder.setType(this.type);
        builder.setToRoomId(this.toRoomId);
        builder.setToUserId(this.toUserId);
        builder.setToDeviceType(this.toDeviceType);
        builder.setTimestamp(this.timestamp);
        builder.setContent(getContent());
        return builder.build().toByteString();
    }
}
