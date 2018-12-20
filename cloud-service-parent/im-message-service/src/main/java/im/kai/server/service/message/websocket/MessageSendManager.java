package im.kai.server.service.message.websocket;

import im.kai.server.api.room.RoomService;
import im.kai.server.domain.ApiResult;
import im.kai.server.domain.user.DeviceType;
import im.kai.server.exception.ServerErrorException;
import im.kai.server.service.message.mq.MqMsg;
import im.kai.server.service.message.protocol.MessageProtos;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.resolver.*;
import im.kai.server.service.message.utils.ServiceUtils;
import im.kai.server.service.message.validator.ValidatorChainFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class MessageSendManager {
    @Autowired
    private UserDeviceSessionManager userDeviceSessionManager;

    @Autowired
    private OnlineUserDeviceManager onlineUserDeviceManager;

    @Autowired(required = false)
    private RoomService mRoomService;

    //发送策略类型：MQ, WEBSOCKET
    public enum SendStrategyType{
        MQ,WEBSOCKET;
    }

    /**
     * MQ消息队列消息处理
     * @param mqMsg MQ消息对象
     */
    public void mqMessageDeal(MqMsg mqMsg){
        Map<DeviceType, WebSocketSessionContext> wsContextMap = userDeviceSessionManager.getAll(mqMsg.getUserId());

        //通过WebSocket发送消息
        MessageSendByWebSocket messageSendStrategy = new MessageSendByWebSocket();
        wsContextMap.forEach(((deviceType, webSocketSessionContext) -> {
            messageSendStrategy.messageSend(webSocketSessionContext, mqMsg.getMqMsg());
        }));
    }

    /**
     * 服务器通知消息(通过MQ发送）
     * @param sessionContext：连接会话上下文
     * @param msgType：通知消息类型
     * @param queueName：MQ队列名
     * @return
     */
    public void noticeMsgSendByMQ(WebSocketSessionContext sessionContext, WebSocketProtos.WebSocketMessage.Type msgType,String queueName){
        //根据消息类型生成消息内容
        MessageResolver messageResolver = MessageResolverFactory.getMessageResolver(msgType);
        byte[] toMsg = (byte[]) messageResolver.resolve(sessionContext,null);

        MqMsg mqMsg = new MqMsg();
        mqMsg.setMqMsg(toMsg);
        mqMsg.setUserId(sessionContext.getAuthToken().getUserId());
        mqMsg.setDeviceTypeId(String.valueOf(sessionContext.getAuthToken().getDevice().getDeviceSubType().getDeviceType().getId()));

        MessageSendByMQ messageSendByMQ = new MessageSendByMQ();
        messageSendByMQ.messageSend(queueName, mqMsg);
}

    /**
     * 发送服务器通知消息（默认通过WebSocket发送）
     * @param sessionContext ：连接会话上下文
     * @param msgType ：通知消息类型
     * @return
     */
    public void noticeMsgSend(WebSocketSessionContext sessionContext, WebSocketProtos.WebSocketMessage.Type msgType){
        //根据消息类型生成消息内容
        MessageResolver messageResolver = MessageResolverFactory.getMessageResolver(msgType);
        byte[] toMsg = (byte[]) messageResolver.resolve(sessionContext,null);

        MessageSendByWebSocket messageSendByWebSocket= new MessageSendByWebSocket();
        messageSendByWebSocket.messageSend(sessionContext,toMsg);
    }

    /**
     * 转发消息(需要根据接收方所在服务器判断发送策略）
     * @param sessionContext 连接会话上下文
     * @param fromMessage 接收到的消息
     */
    public void transferMessageSend(WebSocketSessionContext sessionContext, WebSocketProtos.WebSocketMessage fromMessage){
        try {
            MessageProtos.Message msgData = MessageProtos.Message.parseFrom(fromMessage.getData());

            //通过消息转换器生成转发消息体
            MessageResolver messageResolver = MessageResolverFactory.getMessageResolver(fromMessage.getType());
            byte[] toMsg = (byte[]) messageResolver.resolve(sessionContext, fromMessage);

            //发送群消息
            if(StringUtils.isNotEmpty(msgData.getToRoomId())){
                //找出群ID对应的用户列表
                ApiResult apiRst = mRoomService.findById(msgData.getToRoomId());
                if(apiRst.getCode()==0&&apiRst.getData()!=null){
                    List<Long> userIds = (List<Long>)apiRst.getData();
                    userIds.forEach((uId)->{
                        if(!uId.equals(sessionContext.getAuthToken().getUserId())) {//群消息不发给自己
                            try {
                                this.pointToPoint(sessionContext, toMsg, String.valueOf(uId), fromMessage);
                            } catch (IOException e) {
                                throw new ServerErrorException("通过WebSocket发送数据失败:"+sessionContext.getSession().getId(), e);
                            }
                        }
                    });
                }
            }else if (StringUtils.isNotEmpty(msgData.getToUserId())){//发送点对点消息
                this.pointToPoint(sessionContext, toMsg, msgData.getToUserId(), fromMessage);
            }

            //将消息发给自己的其他设备
            this.pointToPoint(sessionContext, toMsg, sessionContext.getAuthToken().getUserId(), fromMessage);
        } catch (Exception e) {
            throw new ServerErrorException("通过WebSocket发送数据失败:"+sessionContext.getSession().getId(), e);
        }
    }

    /**
     * 点对点发送消息
     * @param sessionContext 连接会话上下文
     * @param toMsg 发送消息体
     * @param toUserId 接收用户userId
     * @param fromMessage 消息来源内容
     */
    public void pointToPoint(WebSocketSessionContext sessionContext, byte[] toMsg, String toUserId,WebSocketProtos.WebSocketMessage fromMessage) throws IOException {
        //如果是聊天类型消息，则要验证好友关系等条件才发送
        if(fromMessage.getType()==WebSocketProtos.WebSocketMessage.Type.SEND_MESSAGE
            && !toUserId.equals(sessionContext.getAuthToken().getUserId())){
            //消息通知生成类
            MessageNoticeResolver messageResolver = (MessageNoticeResolver) MessageResolverFactory.getMessageResolver(WebSocketProtos.
                    WebSocketMessage.Type.MESSAGE_NOTICE);
            //消息发送条件验证，验证通过才发消息
            if(ValidatorChainFactory.getValidatorChainObj(fromMessage.getType()).validateConditions()) {
                this.sendToPointDirect(sessionContext,toMsg,toUserId,fromMessage);
            }else {
                //通知消息拒收
                messageResolver.setNoticeMsgType(WebSocketProtos.MessageNoticeMessage.Type.REJECT);
                byte[] rejectMsgBytes = (byte[]) messageResolver.resolve(sessionContext, fromMessage);
                sessionContext.getSession().sendMessage(new BinaryMessage(rejectMsgBytes));
                return;
            }

            //通知用户消息已转发
            messageResolver.setNoticeMsgType(WebSocketProtos.MessageNoticeMessage.Type.DELIVERY);
            byte[] msgBytes = messageResolver.resolve(sessionContext, fromMessage);
            if(msgBytes!=null) sessionContext.getSession().sendMessage(new BinaryMessage(msgBytes));
        }else{
            this.sendToPointDirect(sessionContext,toMsg,toUserId,fromMessage);
        }
    }

    /**
     * 将消息直接发送给单个userId的多台设备
     * @param sessionContext 连接会话上下文
     * @param toMsg 发送消息体
     * @param toUserId 接收用户userId
     * @param fromMessage 消息来源内容
     */
    public void sendToPointDirect(WebSocketSessionContext sessionContext, byte[] toMsg, String toUserId,WebSocketProtos.WebSocketMessage fromMessage) {
        //通过WebSocket发送消息
        MessageSendByWebSocket messageSendStrategy = new MessageSendByWebSocket();
        //获取需要发送的设备列表
        Map<DeviceType, WebSocketSessionContext> localSessionMap = userDeviceSessionManager.getAll(toUserId);
        localSessionMap.forEach((dvType, wsCtx) -> {
            if(toUserId.equals(sessionContext.getAuthToken().getUserId())) {//不发给自己的同类型设备
                if(!dvType.equals(sessionContext.getAuthToken().getDevice().getDeviceSubType().getDeviceType())) {
                    messageSendStrategy.messageSend(wsCtx, toMsg);
                }
            }else{
                messageSendStrategy.messageSend(wsCtx, toMsg);
            }
        });

        //通过MQ发送消息
        MessageSendByMQ messageSendByMQ = new MessageSendByMQ();
        //创建MQ消息对象
        MqMsg mqMsg = new MqMsg();
        mqMsg.setMqMsg(toMsg);
        mqMsg.setUserId(toUserId);
        //从全局缓存获取接收方信息
        Map<String, String> redisSessionMap = onlineUserDeviceManager.getAll(toUserId);
        redisSessionMap.forEach((deviceType, queueName) -> {
            mqMsg.setDeviceTypeId(deviceType);

            //消息队列名不为空且不等于本地服务名（本地消息队列对应的session会话已经发送过消息）
            if(StringUtils.isNotEmpty(queueName)&&!queueName.equals(ServiceUtils.LOCAL_TCP_SERVICE_NAME)) {
                messageSendByMQ.messageSend(queueName,mqMsg);
            }
        });
    }
}
