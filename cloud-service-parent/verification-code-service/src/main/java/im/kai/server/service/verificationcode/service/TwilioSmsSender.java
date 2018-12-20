package im.kai.server.service.verificationcode.service;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import im.kai.server.service.verificationcode.config.TwilioProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * 通过twilio短信平台发送短信通知
 */
@Slf4j
@Service
@Profile("prod")
@ConditionalOnProperty(prefix = "kai.service.sms", name = "sender", havingValue = "twilio")
public class TwilioSmsSender implements SmsSender {
    private TwilioProperties twilioProperties;
    private Random random;

    public TwilioSmsSender(TwilioProperties properties){
        this.twilioProperties = properties;
        this.random  = new Random(System.currentTimeMillis());
        Twilio.init(properties.getAccountId(), properties.getAuthToken());
    }
    /**
     * 发送短信
     * @param body
     * @param phoneNumber
     */

    @Override
    public void send(String body, String phoneNumber) {
        Assert.notNull(body, body);
        Assert.notNull(phoneNumber, "phone mumber");
        try {
            if (!StringUtils.isEmpty(this.twilioProperties.getMessageServiceSid())) {
                sendNotification(body, phoneNumber);
            } else {
                sendSMS(body, phoneNumber);
            }
        }catch (Exception exception){
            log.error(String.format("send sms to (%s), message: %s", phoneNumber, body), exception);
        }
    }

    /**
     * 发送SMS短信
     * @param body
     * @param phoneNumber
     */
    private void sendSMS(String body, String phoneNumber){
        String pn = getRandomPhoneNumber();
        Assert.notNull(pn, "sms phone number");
        Message msg = Message.creator(
                Promoter.phoneNumberFromString(pn),
                Promoter.phoneNumberFromString(phoneNumber),
                body).create();

        log.info("send sms to ({}) by twilio, result: {}", phoneNumber, msg.getSid());
    }

    /**
     * 通过通知服务发送
     * @param body
     * @param phoneNumber
     */
    private void sendNotification(String body, String phoneNumber){
        Message msg = Message.creator(
                Promoter.phoneNumberFromString(phoneNumber),
                this.twilioProperties.getMessageServiceSid(),
                body).create();
        String sid = msg.getSid();
        log.info("send sms notification to ({}) by twilio, result: {}", phoneNumber, sid);
    }

    /**
     * 获取随便的一个发短信号码
     * @return
     */
    private String getRandomPhoneNumber(){
        String[] phoneNumbers = this.twilioProperties.getFromNumbers();
        if(phoneNumbers != null && phoneNumbers.length > 0){
            return phoneNumbers[this.random.nextInt(phoneNumbers.length)];
        }else{
            return null;
        }
    }


}
