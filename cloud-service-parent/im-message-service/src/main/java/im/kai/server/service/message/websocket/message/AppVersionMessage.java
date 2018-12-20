package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.WebSocketProtos;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * app新版本消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppVersionMessage extends WebSocketMessage {
    /**
     * APP最新版本
     */
    private String version;
    /**
     * 更新日志信息
     */
    private String updateLog;
    /**
     * 新版本APP的更新地址
     */
    private String updateUrl;
    /**
     * 下载模式
     */
    private WebSocketProtos.SystemNoticeMessage.AppVersionMessage.Mode downloadMode;
    /**
     * 更新模式
     */
    private WebSocketProtos.SystemNoticeMessage.AppVersionMessage.Mode updateMode;
    /**
     * 实例化网络数据包
     */
    public AppVersionMessage(){
        super(WebSocketProtos.WebSocketMessage.Type.SYSTEM_NOTICE);
    }

    @Override
    ByteString getData() {
        WebSocketProtos.SystemNoticeMessage.Builder builder = WebSocketProtos.SystemNoticeMessage.newBuilder();
        builder.setType(WebSocketProtos.SystemNoticeMessage.Type.APP_VERSION);
        WebSocketProtos.SystemNoticeMessage.AppVersionMessage.Builder appViersionBuilder = WebSocketProtos.SystemNoticeMessage.AppVersionMessage.newBuilder();
        appViersionBuilder.setVersion(this.version);
        appViersionBuilder.setUpdateLog(ByteString.copyFromUtf8(this.updateLog));
        appViersionBuilder.setUpdateUrl(this.updateUrl);
        appViersionBuilder.setUpdateMode(this.updateMode);
        appViersionBuilder.setDownloadMode(this.downloadMode);
        builder.setData(appViersionBuilder.build().toByteString());
        return builder.build().toByteString();
    }
}
