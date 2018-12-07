package im.kai.server.service.basic.cloudstorage;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * 采用Minio实现云存储方案
 */
@Slf4j
public class MinioUrlSigner implements UrlSigner {
    private CloudStorageProperties configuration;
    private MinioClient client;
    public MinioUrlSigner(CloudStorageProperties configuration){
        this.configuration = configuration;
        this.client = createClient();
    }

    /**
     * 创建客户端
     * @return
     */
    private MinioClient createClient(){
        try {
            MinioClient client =  new MinioClient(configuration.getHost(),
                    configuration.getAccessKey(),
                    configuration.getAccessSecret(),
                    configuration.getRegion()
             );
            if(!client.bucketExists(configuration.getBucket())){
                client.makeBucket(configuration.getBucket(), configuration.getRegion());
            }
            return client;
        } catch (Exception e) {
            log.error("初始化Minio client出错", e);
            return null;
        }
    }

    @Override
    public String presignedPutObjectUrl(String objectName) throws Exception {
        Assert.notNull(this.client, "client");
        return this.client.presignedPutObject(this.configuration.getBucket(),
                objectName,
                this.configuration.getPutDuration());
    }

    @Override
    public String presignedGetObjectUrl(String objectName) throws Exception {
        Assert.notNull(this.client, "client");
        return this.client.presignedGetObject(this.configuration.getBucket(),
                objectName,
                this.configuration.getDownDuration());

    }


    /**
     * 删除文件
     * @param objectName 需要删除的对象名称（文件名称）
     * @throws Exception
     */
    @Override
    public void deleteObject(String objectName) throws Exception{
        Assert.notNull(this.client, "client");
        this.client.removeObject(this.configuration.getBucket(),
                objectName);
    }
}
