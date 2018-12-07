package im.kai.server.service.message.resolver;

import com.google.protobuf.InvalidProtocolBufferException;
import im.kai.server.domain.websocket.WebSocketAuthToken;
import im.kai.server.exception.ServerErrorException;
import im.kai.server.service.message.protocol.MessageProtos;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * 7 = 与消息相关的通知包
 */
@Component
public class MessageNoticeResolver implements MessageResolver<WebSocketProtos.WebSocketMessage,byte[]> {
    @Override
    public WebSocketProtos.WebSocketMessage.Type getType() {
        return WebSocketProtos.WebSocketMessage.Type.MESSAGE_NOTICE;
    }

    private WebSocketProtos.MessageNoticeMessage.Type noticeMsgType;

    public WebSocketProtos.MessageNoticeMessage.Type getNoticeMsgType() {
        return noticeMsgType;
    }

    public void setNoticeMsgType(WebSocketProtos.MessageNoticeMessage.Type noticeMsgType) {
        this.noticeMsgType = noticeMsgType;
    }

    @Override
    public byte[] resolve(WebSocketSessionContext sessionContext, WebSocketProtos.WebSocketMessage message) {
        WebSocketAuthToken authToken = sessionContext.getAuthToken();
        try {
            //获取来的文本信息
            MessageProtos.Message fromTextMss = MessageProtos.Message.parseFrom(message.getData());
            //创建发送的文本信息
            WebSocketProtos.MessageNoticeMessage.Builder msgNoticeMsg = WebSocketProtos.MessageNoticeMessage.newBuilder();
            msgNoticeMsg.setType(this.noticeMsgType);
            msgNoticeMsg.setSeq(fromTextMss.getSeq());
            if(noticeMsgType.equals(WebSocketProtos.MessageNoticeMessage.Type.READ)
                    ||noticeMsgType.equals(WebSocketProtos.MessageNoticeMessage.Type.TAKE_BACK)){//3.消息已读；4.消息撤回
                msgNoticeMsg.setFromUserId(authToken.getUserId());
                msgNoticeMsg.setFromRoomId(fromTextMss.getToRoomId());
                msgNoticeMsg.setFromDeviceType(authToken.getDevice().getDeviceSubType().getId());
            }else{//1.消息拒收；2.消息已转发；
                msgNoticeMsg.setToUserId(fromTextMss.getToUserId());
                msgNoticeMsg.setToRoomId(fromTextMss.getToRoomId());
                msgNoticeMsg.setToDeviceType(fromTextMss.getToDeviceType());
            }
            msgNoticeMsg.setTimestamp(Instant.now().getEpochSecond());

            //更新接受到的信息，转为发送格式
            WebSocketProtos.WebSocketMessage.Builder builder = message.toBuilder();
            builder.setType(getType());
            builder.setData(msgNoticeMsg.build().toByteString());
            return builder.build().toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new ServerErrorException("解析SEND_MESSAGE类型数据失败", e);
        }
    }
}
