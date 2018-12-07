package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

/*
文件消息定义
 */
public class UserChatFileMessage extends UserChatMessage {
    /**
     * 附件id
     */
    private String id;
    /**
     * 文件类型（如：audio/mp3 | audio/x-aiff)
     */
    private String contentType;
    /**
     * 文件大小
     */
    private int size;
    /**
     * 文件名称
     */
    private String fileName;
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
        MessageProtos.FileMessage.Builder builder = MessageProtos.FileMessage.newBuilder();
        try {
            builder.setId(this.id);
            builder.setContentType(this.contentType);
            builder.setSize(this.size);
            builder.setFileName(ByteString.copyFrom(this.fileName,"UTF-8"));
            builder.setUrl(this.url);
            builder.setMd5(this.md5);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return builder.build().toByteString();
    }
}
