package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

import java.io.UnsupportedEncodingException;

/*
图片消息定义
 */
public class UserChatImageMessage extends UserChatMessage {
    /**
     * 附件id
     */
    private String id;
    /**
     * 图片类型（如：image/jpeg | image/png | image/gif)
     */
    private String contentType;
    /**
     * 图片大小
     */
    private int size;
    /**
     *图片的原始宽度
     */
    private int width;
    /**
     *图片的原始高度
     */
    private int height;
    /**
     * 图片缩略图
     */
    private String thumbnail;
    /**
     * 图片的原图地址
     */
    private String url;
    /**
     * 文件的md5 hash值
     */
    private String md5;
    /**
     * 文件名称
     */
    private String fileName;

    @Override
    ByteString getContent() {
        try {
            MessageProtos.ImageMessage.Builder builder = MessageProtos.ImageMessage.newBuilder();
            builder.setId(this.id);
            builder.setContentType(this.contentType);
            builder.setSize(this.size);
            builder.setWidth(this.width);
            builder.setHeight(this.height);
            builder.setThumbnail(ByteString.copyFrom(this.thumbnail,"UTF-8"));
            builder.setUrl(this.url);
            builder.setMd5(this.md5);
            builder.setFileName(ByteString.copyFrom(this.fileName,"UTF-8"));
            return builder.build().toByteString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
