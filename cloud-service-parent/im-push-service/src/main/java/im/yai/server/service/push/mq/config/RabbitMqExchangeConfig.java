package im.kai.server.service.push.mq.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 16:35
 * 用于配置交换机和队列对应关系(生产者配置)
 */
@Configuration
@AutoConfigureAfter(RabbitMqConfig.class)
public class RabbitMqExchangeConfig {



    /**
     * topic主题交换机实例化
     *
     * @return
     */
    @Bean
    public TopicExchange contractTopicExchangeDurable() {
        TopicExchange contractTopicExchange = new TopicExchange(RabbitMqExchangeEnum.EXCHANGE_TOPIC.getCode(), true, false);
        return contractTopicExchange;
    }

    /**
     * direct直连交换机实例化
     *
     * @param
     * @return
     */
    @Bean
    public DirectExchange contractDirectExchange() {
        DirectExchange contractDirectExchange = new DirectExchange(RabbitMqExchangeEnum.EXCHANGE_DIRECT.getCode(), true, false);
        return contractDirectExchange;
    }

    /**
     * 事物队列
     *
     * @param
     * @return
     */
    @Bean
    public Queue transQueue( ) {
        Queue queue = new Queue(RabbitMqQueueNameEnum.MESSAGE_TRANS_QUEUE.getCode(), true, false, true, null);
        return queue;
    }

    /**
     * 事物队列绑定
     *
     * @param transQueue  消息中心队列
     * @param
     * @return
     */
    @Bean
    public Binding transBinding(Queue transQueue) {
        Binding binding = new Binding(RabbitMqQueueNameEnum.MESSAGE_TRANS_QUEUE.getCode(), Binding.DestinationType.QUEUE, RabbitMqExchangeEnum.EXCHANGE_TOPIC.getCode(), RabbitMqRoutingKeyEnum.MESSAGE_TRANS_QUEUE.getCode(), null);
        return binding;
    }

    /**
     * 延时队列
     *
     * @param
     * @return
     */
    @Bean
    public Queue messageQueue( ) {
        Queue queue = new Queue(RabbitMqQueueNameEnum.MESSAGE_QUEUE.getCode());
        return queue;
    }

    /**
     * 转发延时队列
     *
     * @return
     */
    @Bean
    public Queue messageTtlQueue( ) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", RabbitMqExchangeEnum.EXCHANGE_DIRECT.getCode());
        arguments.put("x-dead-letter-routing-key", RabbitMqRoutingKeyEnum.MESSAGE_QUEUE.getCode());
        Queue queue = new Queue(RabbitMqQueueNameEnum.MESSAGE_TTL_QUEUE.getCode(), true, false, false, arguments);
        return queue;
    }

    /**
     * 消息中心实际消息交换与队列绑定
     *
     * @param exchange     消息中心交换配置
     * @param messageQueue 消息中心队列
     * @param
     * @return
     */
    @Bean
    public Binding messageBinding(Queue messageQueue, DirectExchange exchange ) {
        Binding binding = BindingBuilder.bind(messageQueue).to(exchange).with(RabbitMqRoutingKeyEnum.MESSAGE_QUEUE.getCode());
        return binding;
    }

    /**
     * 消息中心TTL绑定实际消息中心实际消费交换机
     * @param messageTtlQueue
     * @param exchange
     * @param
     * @return
     */
    @Bean
    public Binding messageTtlBinding(Queue messageTtlQueue, DirectExchange exchange ) {
        Binding binding = BindingBuilder.bind(messageTtlQueue).to(exchange).with(RabbitMqRoutingKeyEnum.MESSAGE_TTL_QUEUE.getCode());
        return binding;
    }

    /**
     * 直连测试队列
     *
     * @param
     * @return
     */
    @Bean
    public Queue queueTest( ) {
        Queue queue = new Queue(RabbitMqQueueNameEnum.TESTQUEUE.getCode());
        return queue;
    }

    /**
     * topic 1 队列
     *
     * @param
     * @return
     */
    @Bean
    public Queue queueTopicTest1( ) {
        Queue queue = new Queue(RabbitMqQueueNameEnum.TOPICTEST1.getCode());
        return queue;
    }

    /**
     * topic 2队列
     *
     * @param
     * @return
     */
    @Bean
    public Queue queueTopicTest2( ) {
        Queue queue = new Queue(RabbitMqQueueNameEnum.TOPICTEST2.getCode());
        return queue;
    }

    /**
     * 直连测试队列和direct交换机绑定
     *
     * @param queueTest
     * @param exchange
     * @param
     * @return
     */
    @Bean
    public Binding bindingQueueTest(Queue queueTest, DirectExchange exchange ) {
        Binding binding = BindingBuilder.bind(queueTest).to(exchange).with(RabbitMqRoutingKeyEnum.TESTQUEUE.getCode());
        return binding;
    }

    /**
     * topic1队列和topic(话题)交换机绑定
     *
     * @param
     * @param
     * @param
     * @return
     */
    @Bean
    public Binding bindingQueueTopicTest1() {
        Binding binding = BindingBuilder.bind(queueTopicTest1()).to(contractTopicExchangeDurable()).with(RabbitMqRoutingKeyEnum.TESTTOPICQUEUE1.getCode());
        return binding;
    }

    /**
     * topic2队列和topic(话题)交换机绑定
     *
     * @param
     * @param
     * @param
     * @return
     */
    @Bean
    public Binding bindingQueueTopicTest2() {
        Binding binding = BindingBuilder.bind(queueTopicTest2()).to(contractTopicExchangeDurable()).with(RabbitMqRoutingKeyEnum.TESTTOPICQUEUE2.getCode());
        return binding;
    }

}