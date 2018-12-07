package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

import java.io.UnsupportedEncodingException;

/*
音频消息定义
 */
public class UserChatAudioMessage extends UserChatMessage {
    /**
     * 附件id
     */
    private String id;
    /**
     * 音频类型（如：audio/mp3 | audio/x-aiff)
     */
    private String contentType;
    /**
     * 音频大小
     */
    private int size;
    /**
     *音频时长
     */
    private int duration;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 文件的md5 hash值
     */
    private String md5;

    @Override
    ByteString getContent() {
        MessageProtos.AudioMessage.Builder builder = MessageProtos.AudioMessage.newBuilder();
        builder.setId(this.id);
        builder.setContentType(this.contentType);
        builder.setSize(this.size);
        builder.setDuration(this.duration);
        builder.setUrl(this.url);
        builder.setMd5(this.md5);
        return builder.build().toByteString();
    }
}
