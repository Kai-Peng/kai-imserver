package im.kai.server.service.basic.cloudstorage;

import lombok.Data;

/**
 * 云存储配置信息
 */
@Data
public class CloudStorageProperties {
    /**
     * 存储文件的云存储服务域名
     */
    private String host;
    /**
     * 接口访问密钥
     */
    private String accessKey;
    /**
     * 接口访问密匙
     */
    private String accessSecret;
    /**
     * 存储桶
     */
    private String bucket;
    /**
     * 存储区
     */
    private String region;
    /**
     * 文件上传地址的过期时间
     */
    private int putDuration;
    /**
     * 文件访问的过期时间
     */
    private int downDuration;

}
