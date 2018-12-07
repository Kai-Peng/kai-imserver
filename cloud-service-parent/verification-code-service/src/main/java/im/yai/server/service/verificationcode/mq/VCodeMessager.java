package im.kai.server.service.verificationcode.mq;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 验证码的消息管理
 */
public interface VCodeMessager {
    String CONSUMER = "vCodeConsumer";
    String PRODUCTER = "vCodeProducter";

    /**
     * 消息消费者
     * @return
     */
    @Input(CONSUMER)
    SubscribableChannel vCodeConsumer();

    /**
     * 消息生产者
     * @return
     */
    @Output(PRODUCTER)
    @Qualifier("vCodeProducter")
    MessageChannel vCodeProducter();
}
