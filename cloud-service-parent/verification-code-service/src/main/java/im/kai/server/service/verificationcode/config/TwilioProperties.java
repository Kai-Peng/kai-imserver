package im.kai.server.service.verificationcode.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Component
@Validated
@ConfigurationProperties("kai.service.sms.twilio")
@Data
@RefreshScope
public class TwilioProperties {
    /**
     * 账号id
     */
    @NotNull
    private String accountId;
    /**
     * 账号授权码
     */
    @NotNull
    private String authToken;
    /**
     * 发送短信通知的服务sid
     */
    private String messageServiceSid;
    /**
     * 发送短信的手机号码列表
     */
    private String[] fromNumbers;

}