package im.kai.server.service.message.resolver;

import com.google.protobuf.InvalidProtocolBufferException;
import im.kai.server.domain.websocket.WebSocketAuthToken;
import im.kai.server.exception.ServerErrorException;
import im.kai.server.service.message.protocol.MessageProtos;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 消息发送
 */
@Component
public class SendMessageResolver implements MessageResolver<WebSocketProtos.WebSocketMessage, byte[]> {
    @Override
    public WebSocketProtos.WebSocketMessage.Type getType() {
        return WebSocketProtos.WebSocketMessage.Type.SEND_MESSAGE;
    }

   @Override
    public byte[] resolve(WebSocketSessionContext sessionContext, WebSocketProtos.WebSocketMessage message) {
        WebSocketAuthToken authToken = sessionContext.getAuthToken();

       try {
           //获取消息体中的data
           MessageProtos.Message fromData = MessageProtos.Message.parseFrom(message.getData());
           //创建发送的文本信息
           MessageProtos.Message.Builder toMssBuilder = MessageProtos.Message.newBuilder();
           toMssBuilder.setSeq(fromData.getSeq());
           toMssBuilder.setType(fromData.getType());
           toMssBuilder.setFromRoomId(fromData.getToRoomId());
           toMssBuilder.setFromUserId(authToken.getUserId());
           toMssBuilder.setFromDeviceType(authToken.getDevice().getDeviceSubType().getId());
           toMssBuilder.setTimestamp(fromData.getTimestamp());
           toMssBuilder.setContent(fromData.getContent());

           //更新接受到的信息，转为发送格式
           WebSocketProtos.WebSocketMessage.Builder builder = message.toBuilder();
           builder.setData(toMssBuilder.build().toByteString());
           return builder.build().toByteArray();
       } catch (InvalidProtocolBufferException e) {
           throw new ServerErrorException("解析SEND_MESSAGE类型数据失败", e);
       }
   }
}
