package im.kai.server.service.basic.config;

import im.kai.server.service.basic.cloudstorage.CloudStorageProperties;
import im.kai.server.service.basic.cloudstorage.MinioUrlSigner;
import im.kai.server.service.basic.cloudstorage.UrlSigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio的云存储服务
 */
@Configuration
public class MinioConfiguration {
    /**
     * 头像的云存储
     * @param avatarStorageConfiguration
     * @return
     */
    @Bean
    public UrlSigner avatarUrlSigner(
            CloudStorageProperties avatarStorageConfiguration){
        return new MinioUrlSigner(avatarStorageConfiguration);
    }
    /**
     * 图像的云存储
     * @param imageStorageConfiguration
     * @return
     */
    @Bean
    public UrlSigner imageUrlSigner(
            CloudStorageProperties imageStorageConfiguration){
        return new MinioUrlSigner(imageStorageConfiguration);
    }
    /**
     * 视频的云存储
     * @param videoStorageConfiguration
     * @return
     */
    @Bean
    public UrlSigner videoUrlSigner(
            CloudStorageProperties videoStorageConfiguration){
        return new MinioUrlSigner(videoStorageConfiguration);
    }
    /**
     * 文件的云存储
     * @param fileStorageConfiguration
     * @return
     */
    @Bean
    public UrlSigner fileUrlSigner(
            CloudStorageProperties fileStorageConfiguration){
        return new MinioUrlSigner(fileStorageConfiguration);
    }
    /**
     * 音频的云存储
     * @param audioStorageConfiguration
     * @return
     */
    @Bean
    public UrlSigner audioUrlSigner(
            CloudStorageProperties audioStorageConfiguration){
        return new MinioUrlSigner(audioStorageConfiguration);
    }
}
