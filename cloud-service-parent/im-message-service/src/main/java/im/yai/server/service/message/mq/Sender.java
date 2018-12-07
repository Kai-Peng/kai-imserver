package im.kai.server.service.message.mq;

import java.util.Date;

import com.google.protobuf.ByteString;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
	 @Autowired    
	 private AmqpTemplate rabbitTemplate;

	/**
	 * 发送消息到对应的消息队列
	 * @param queue
	 * @param mqMsg
	 */
	public void sendText(String queue, MqMsg mqMsg) {
		 this.rabbitTemplate.convertAndSend(queue, mqMsg);
	 }
}
