package im.kai.server.service.message.mq;

import im.kai.server.service.message.utils.ServiceUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

@Configuration
public class RabbitConfig {
	@Value("${mq.rabbit.exchange.name:test}")
	String exchangeName;
	/*@Value("${mq.rabbit.address}")
	String address;
	@Value("${mq.rabbit.username}")
	String username;
	@Value("${mq.rabbit.password}")
	String password;
	@Value("${mq.rabbit.virtualHost}")
	String mqRabbitVirtualHost;
	@Value("${mq.rabbit.exchange.name}")
	String exchangeName;
	@Value("${mq.rabbit.size}")
	int queueSize;
	@Value("${mq.concurrent.consumers}")
	int concurrentConsumers;
	@Value("${mq.prefetch.count}")
	int prefetchCount;

	//创建mq连接
	@Bean(name="connectionFactory")
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(mqRabbitVirtualHost);
		connectionFactory.setPublisherConfirms(true);
		connectionFactory.setAddresses(address);

		return connectionFactory;
	}

	@Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
	*/

	@Autowired
	public ConnectionFactory connectionFactory;

	@Bean
	public Queue tcpServiceQueue() {
		return new Queue(ServiceUtils.LOCAL_TCP_SERVICE_NAME);
	}

	@Bean
	public TopicExchange exchange(){
		return new TopicExchange(exchangeName);
	}

	@Bean
	Binding bindingExchangeMessage(Queue queue, TopicExchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with(ServiceUtils.LOCAL_TCP_SERVICE_NAME);
	}

	//创建监听器，监听队列
	@Bean
	public SimpleMessageListenerContainer mqMessageContainer(ReceiverService receiverService) throws AmqpException, IOException {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
    	container.setQueueNames(ServiceUtils.LOCAL_TCP_SERVICE_NAME);
		container.setExposeListenerChannel(true);
		container.setMessageListener(receiverService);//监听处理类
		/*container.setPrefetchCount(prefetchCount);//设置每个消费者获取的最大的消息数量
		container.setConcurrentConsumers(concurrentConsumers);//消费者个数
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式为手工确认*/

		return container;
	}

	//监听处理类
	@Bean
	@Scope("prototype")
	public ReceiverService receiverService(){
		return new ReceiverService();
	}
}
