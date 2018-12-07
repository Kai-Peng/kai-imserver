package im.kai.server.service.push.mq.config;

import com.rabbitmq.client.Channel;
import im.kai.server.service.push.domain.NoticeMessage;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 16:29
 * 消费者配置
 */
@Configuration
@AutoConfigureAfter(RabbitMqConfig.class)
public class ExampleAmqpConfiguration {


    @Bean("testQueueContainer")
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMqQueueNameEnum.TESTQUEUE.getCode());
        container.setMessageListener(exampleListener());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }


    @Bean("testQueueListener")
    public ChannelAwareMessageListener exampleListener() {
        return new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        };
    }
}
