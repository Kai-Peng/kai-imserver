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
@ConfigurationProperties("yai.service.sms.nexmo")
@Data
@RefreshScope
public class NexmoProperties {
    /**
     * apiKey
     */
    @NotNull
    private String apiKey;
    /**
     * api密钥
     */
    @NotNull
    private String apiSecret;
    /**
     * 来源
     */
    @NotNull
    private String from;

}