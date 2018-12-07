package im.kai.server.service.push.mq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 16:32
 * RabbitMq配置文件读取类
 */
public class RabbitMqConfig {

    /** ip地址 */
    @Value("${spring.rabbitmq.host}")
    private String host;

    /** 端口号 */
    @Value("${spring.rabbitmq.port}")
    private int port;

    /** 用户名 */
    @Value("${spring.rabbitmq.username}")
    private String username;

    /** 密码 */
    @Value("${spring.rabbitmq.password}")
    private String password;

    /** 虚拟机 */
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    /** publisher确认开关 */
    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    /**
     * 实例化rabbitmq工厂
     * @return ConnectionFactory
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        //如果要进行消息回调，则这里必须要设置为true 如果要开启事物必须注释掉,rabbitmq事物消息使用时不能开启自动回查确认机制*
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }
    /**
     * RabbitTemplate 初始化
     * @return RabbitTemplate
     * 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        template.setChannelTransacted(true);
        return template;
    }
}
