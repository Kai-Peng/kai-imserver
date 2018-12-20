package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

import java.io.UnsupportedEncodingException;

/*
视频消息定义
 */
public class UserChatVideoMessage extends UserChatMessage {
    /**
     * 附件id
     */
    private String id;
    /**
     * 视频类型（如：video/3gp | video/mp4)
     */
    private String contentType;
    /**
     * 文件大小
     */
    private int size;
    /**
     *视频时长
     */
    private int duration;
    /**
     *视频的封面图地址
     */
    private String thumbnail;
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
        MessageProtos.VideoMessage.Builder builder = null;
        try {
            builder = MessageProtos.VideoMessage.newBuilder();
            builder.setId(this.id);
            builder.setContentType(this.contentType);
            builder.setSize(this.size);
            builder.setDuration(this.duration);
            builder.setThumbnail(ByteString.copyFrom(this.thumbnail, "UTF-8"));
            builder.setUrl(this.url);
            builder.setMd5(this.md5);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return builder.build().toByteString();
    }
}
