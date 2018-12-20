package im.kai.server.service.basic.config;

import im.kai.server.service.basic.cloudstorage.CloudStorageProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 附件存储的配置信息
 */
@Configuration
@RefreshScope
public class AttachmentsConfiguration {
    /**
     * 存储头像的配置信息
     * @return
     */
    @Bean
    @Qualifier("avatarStorageConfiguration")
    @ConfigurationProperties(prefix = "yai.service.attachments.avatar")
    public CloudStorageProperties avatarStorageConfiguration(){
        return new CloudStorageProperties();
    }
    /**
     * 存储图片的配置信息
     * @return
     */
    @Bean
    @Qualifier("imageStorageConfiguration")
    @ConfigurationProperties(prefix = "yai.service.attachments.image")
    public CloudStorageProperties imageStorageConfiguration(){
        return new CloudStorageProperties();
    }
    /**
     * 存储图片的配置信息
     * @return
     */
    @Bean
    @Qualifier("videoStorageConfiguration")
    @ConfigurationProperties(prefix = "yai.service.attachments.video")
    public CloudStorageProperties videoStorageConfiguration(){
        return new CloudStorageProperties();
    }

    /**
     * 存储文件的配置信息
     * @return
     */
    @Bean
    @Qualifier("fileStorageConfiguration")
    @ConfigurationProperties(prefix = "yai.service.attachments.file")
    public CloudStorageProperties fileStorageConfiguration(){
        return new CloudStorageProperties();
    }
    /**
     * 存储音频的配置信息
     * @return
     */
    @Bean
    @Qualifier("audioStorageConfiguration")
    @ConfigurationProperties(prefix = "yai.service.attachments.audio")
    public CloudStorageProperties audioStorageConfiguration(){
        return new CloudStorageProperties();
    }
}
