package im.kai.server.service.verificationcode.service;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
import im.kai.server.service.verificationcode.config.NexmoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 通过Nexmo的短信平台发送短信通知
 */
@Slf4j
@Service
@Profile("prod")
@ConditionalOnProperty(prefix = "kai.service.sms", name = "sender", havingValue = "nexmo")
public class NexmoSmsSender implements SmsSender {

    private NexmoProperties nexmoProperties;
    private NexmoClient nexmoClient;
    public NexmoSmsSender(NexmoProperties properties) {
        this.nexmoProperties = properties;
        this.nexmoClient = new NexmoClient.Builder()
                .apiKey(properties.getApiKey())
                .apiSecret(properties.getApiSecret())
                .build();

    }


    @Override
    public void send(String body, String phoneNumber) {
        Assert.notNull(body, "sms body");
        Assert.notNull(phoneNumber, "phone mumber");
        if(phoneNumber.startsWith("+")) phoneNumber = phoneNumber.substring(1);

        try{
            TextMessage msg = new TextMessage(
                    this.nexmoProperties.getFrom(),
                    phoneNumber,
                    body);

            SmsSubmissionResponse response = this.nexmoClient.getSmsClient().submitMessage(msg);
            if(response.getMessageCount() > 0){
                SmsSubmissionResponseMessage responseMessage = response.getMessages().get(0);
                if(responseMessage.getStatus() != MessageStatus.OK){
                    log.error(String.format("send sms to (%s), message: %s , error {}", phoneNumber, body), responseMessage);
                    return;
                }
                log.info("send sms to ({}) by nexmo, result: {}, price: {}",
                        phoneNumber,
                        responseMessage.getId(),
                        responseMessage.getMessagePrice());
            }
        }catch (Exception ex){
            log.error(String.format("send sms to (%s), message: %s", phoneNumber, body), ex);
        }
    }
}
