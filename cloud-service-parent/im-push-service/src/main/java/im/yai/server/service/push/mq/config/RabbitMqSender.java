package im.kai.server.service.push.mq.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 16:37
 *  rabbitmq发送消息工具类
 */
@Slf4j
@Component
public class RabbitMqSender implements RabbitTemplate.ConfirmCallback {

    /**
     * rabbitmq模板
     */
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 直连 指定routekey的指定queue
     * @param routeKey
     * @param obj
     */
    public void sendRabbitmqDirect(String routeKey, Object obj) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("send: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend(RabbitMqExchangeEnum.EXCHANGE_DIRECT.getCode(), routeKey, obj, correlationData);
    }

    /**
     * 所有发送到Topic Exchange的消息被转发到所有关心RouteKey中指定Topic的Queue上
     * @param routeKey
     * @param obj
     */
    public void sendRabbitmqTopic(String routeKey, Object obj) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("send: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend(RabbitMqExchangeEnum.EXCHANGE_TOPIC.getCode(), routeKey, obj, correlationData);
    }

    /**
     * 发布消息后进行回调
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 回调id:" + correlationData.getId());
        if (ack) {
            log.info("发布消息后进行回调 消息成功消费");
        } else {
            log.info("发布消息后进行回调 消息消费失败:" + cause);
        }
    }
}
