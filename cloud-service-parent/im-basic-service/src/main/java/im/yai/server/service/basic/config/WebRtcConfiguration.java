package im.kai.server.service.basic.config;

import im.kai.server.service.basic.turn.TurnProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class WebRtcConfiguration {
    /**
     * 存储头像的配置信息
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "yai.service.webrtc.turn")
    public TurnProperties turnConfiguration(){
        return new TurnProperties();
    }

}
