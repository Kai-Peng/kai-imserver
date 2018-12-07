package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.WebSocketProtos;

/**
 * 用户下线的消息
 */
public class UserOfflineMessage extends WebSocketMessage {

    private WebSocketProtos.UserOfflineMessage.Reason reason;
    private String deivceName;
    private String remoteAddress;

    /**
     * 获取新登录设备的名称
     * @return
     */
    public String getDeivceName() {
        return deivceName;
    }

    /**
     * 设置新登录账号的设备名称
     * @param deivceName
     */
    public void setDeivceName(String deivceName) {
        this.deivceName = deivceName;
    }

    /**
     * 获取新登录账号的ip地址
     * @return
     */
    public String getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * 设置新登录账号的ip地址
     * @param remoteAddress
     */
    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    /**
     * 实例化网络数据包
     */
    public UserOfflineMessage(WebSocketProtos.UserOfflineMessage.Reason reason){
        super(WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE);
        this.reason = reason;
    }

    @Override
    ByteString getData() {
        WebSocketProtos.UserOfflineMessage.Builder builder = WebSocketProtos.UserOfflineMessage.newBuilder();
        builder.setReason(this.reason);
        builder.setDeivceName(this.getDeivceName());
        builder.setRemoteAddress(this.getRemoteAddress());
        return builder.build().toByteString();
    }
}
