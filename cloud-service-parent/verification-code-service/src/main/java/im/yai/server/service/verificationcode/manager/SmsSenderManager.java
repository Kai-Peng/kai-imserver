package im.kai.server.service.verificationcode.manager;

import im.kai.server.service.verificationcode.service.NexmoSmsSender;
import im.kai.server.service.verificationcode.service.SmsSender;
import im.kai.server.service.verificationcode.service.TwilioSmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 短信通知发送的管理者
 */
@Slf4j
@Service
public class SmsSenderManager {

    @Autowired(required = false)
    private SmsSender smsSender;

    /**
     * 发送短信通知
     * @param body
     * @param phoneNumber
     */
    public void send(String body, String phoneNumber) {
        if(smsSender != null){
            smsSender.send(body, phoneNumber);
        }else{
            log.info("send sms to ({}) by empty sender, message: {}", phoneNumber, body);
        }
    }
}
