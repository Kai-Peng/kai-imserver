package im.kai.server.service.message.mq;

import com.rabbitmq.client.Channel;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.utils.ServiceUtils;
import im.kai.server.service.message.websocket.MessageSendManager;
import im.kai.server.service.message.websocket.UserDeviceSessionManager;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import im.kai.server.service.message.websocket.WebSocketSessionContextManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReceiverService implements ChannelAwareMessageListener {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private MessageSendManager messageSendManager;

    @Autowired
    private WebSocketSessionContextManager webSocketSessionContextManager;

    @Autowired
    private UserDeviceSessionManager userDeviceSessionManager;

    private volatile MessageConverter messageConverter = new org.springframework.amqp.support.converter.SimpleMessageConverter();

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            //转换成发送的对象格式
            MqMsg mqMsg = (MqMsg) messageConverter.fromMessage(message);

            //消费消息, 消费返回状态:0-消费成功，1-可重试失败处理，2-消费失败
            int msgState = this.processMsg(mqMsg);

            if(msgState==0){//确认消息消费成功
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else if(msgState==1){//可重试的失败处理
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            }else{//消费失败
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            }
        } catch (Exception e) {
            //丢弃消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            e.printStackTrace();
        }
    }

    private int processMsg(MqMsg mqMsg){
        //发送消息
        messageSendManager.mqMessageDeal(mqMsg);

        WebSocketProtos.WebSocketMessage webSocketMessage;
        try {
            webSocketMessage = WebSocketProtos.WebSocketMessage.parseFrom(mqMsg.getMqMsg());
            if(webSocketMessage.getType()==WebSocketProtos.WebSocketMessage.Type.USER_OFFLINE){
                //如果是用户下线消息，需要更新本地缓存
                WebSocketSessionContext offlineSession = userDeviceSessionManager.remove(mqMsg.getUserId(),Integer.valueOf(mqMsg.getDeviceTypeId()));
                webSocketSessionContextManager.removeContextMap(offlineSession.getSession().getId());
            }
        }catch (Exception ex){
            log.error("解析接收到的MQ消息失败:"+ ServiceUtils.LOCAL_TCP_SERVICE_NAME, ex);
        }

        return 0;
    }
}
