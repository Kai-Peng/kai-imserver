package im.kai.server.service.message.resolver;

import im.kai.server.service.message.mq.MqMsg;
import im.kai.server.service.message.mq.Sender;
import im.kai.server.service.message.protocol.WebSocketProtos;
import im.kai.server.service.message.validator.ValidatorChainFactory;
import im.kai.server.service.message.websocket.WebSocketSessionContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSendByMQ{
    //消息队列发送工具类
    @Autowired
    private Sender sender;

    /**
     * 将消息发送到消息队列
     * @param queueName 消息队列名
     * @param mqMsg 消息对象
     */
    public void messageSend(String queueName, MqMsg mqMsg) {
        if (StringUtils.isNotEmpty(queueName)) {
            sender.sendText(queueName, mqMsg);
        }
    }
}
