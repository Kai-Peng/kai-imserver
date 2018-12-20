package im.kai.server.service.verificationcode.mq;

import im.kai.server.service.verificationcode.bean.VCodeCreatedMessage;
import im.kai.server.service.verificationcode.manager.SmsSenderManager;
import im.kai.server.service.verificationcode.service.NexmoSmsSender;
import im.kai.server.service.verificationcode.service.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 验证码消息消费者，用于发送验证码
 */
@Slf4j
@Component
public class VCodeMessageConsumer {
    @Autowired
    private SmsSenderManager smsSenderManager;
    /**
     * 消息模板
     */
    @Value("${yai.service.sms.vcode-message}")
    private String vcodeMessage;
    /**
     * 接收到mq消息
     * @param message
     */
    @StreamListener(VCodeMessager.CONSUMER)
    public void receiver(VCodeCreatedMessage message){
        log.info("Got a {} vcode: {}={}", message.getMode(), message.getMobile(), message.getVCode());
        if(message.getMode() == VCodeCreatedMessage.Mode.SMS){
            smsSenderManager.send(String.format(vcodeMessage, message.getVCode()), message.getMobile());
        }
    }
}
