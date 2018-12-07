package im.kai.server.service.push.mq.config;
import com.rabbitmq.client.Channel;
import im.kai.server.service.push.domain.NoticeMessage;
import im.kai.server.service.push.service.imp.PushServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 16:38
 *消费者配置
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RabbitMqConfig.class)
public class TopicAmqpConfiguration {


   @Autowired
   private PushServiceImp mPushServiceImp;

    @Bean("topicTest1Container")
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMqQueueNameEnum.TOPICTEST1.getCode());
        container.setMessageListener(exampleListener1());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }


    /**
     * 监听mq  推送apn
     * @return
     */
    @Bean("topicTest1Listener")
    public ChannelAwareMessageListener exampleListener1(){
        return new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                NoticeMessage noticeMessage = (NoticeMessage) SerializeUtil.unserialize(message.getBody());
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                mPushServiceImp.pushToApns(noticeMessage);
            }
        };
    }
}
