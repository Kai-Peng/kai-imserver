package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.MessageProtos;

/*
通话消息定义
 */
public class UserChatCallMessage extends UserChatMessage {
    /**
     * 通话消息类型
     */
    private MessageProtos.CallMessage.Type type;
    /**
     * data
     */
    private ByteString data;
    /**
     * sdp
     */
    private String description;
    /**
     * sdp_mid
     */
    private String sdpMid;
    /**
     * sdp_mline_index
     */
    private int sdpMlineIndex;
    /**
     * sdp
     */
    private String sdp;

    @Override
    ByteString getContent() {
        MessageProtos.CallMessage.Builder builder = MessageProtos.CallMessage.newBuilder();
        builder.setType(this.type);
        if(this.type.equals(MessageProtos.CallMessage.Type.OFFER)){
            MessageProtos.CallMessage.Offer.Builder offerBuilder = MessageProtos.CallMessage.Offer.newBuilder();
            offerBuilder.setDescription(this.description);
            builder.setData(offerBuilder.build().toByteString());
        }else if(this.type.equals(MessageProtos.CallMessage.Type.ANSWER)){
            MessageProtos.CallMessage.Answer.Builder answerBuilder = MessageProtos.CallMessage.Answer.newBuilder();
            answerBuilder.setDescription(this.description);
            builder.setData(answerBuilder.build().toByteString());
        }else if(this.type.equals(MessageProtos.CallMessage.Type.UPDATE)){
            MessageProtos.CallMessage.IceUpdate.Builder iceUpdateBuilder = MessageProtos.CallMessage.IceUpdate.newBuilder();
            iceUpdateBuilder.setSdp(this.sdp);
            iceUpdateBuilder.setSdpMid(this.sdpMid);
            iceUpdateBuilder.setSdpMlineIndex(this.sdpMlineIndex);
            builder.setData(iceUpdateBuilder.build().toByteString());
        }

        return builder.build().toByteString();
    }
}
