package im.kai.server.service.basic.cloudstorage;

import java.util.Map;

/**
 * 云存储的地址签名者
 */
public interface UrlSigner {
    /**
     * 获取一个预签名可用于上传文件的地址
     * @param objectName 上传的对象名称（文件名称）
     * @return URL地址
     * @throws Exception
     */
    String presignedPutObjectUrl(String objectName) throws Exception;
    /**
     * 获取一个预签名可用于访问下载文件的地址
     * @param objectName 下载的对象名称（文件名称）
     * @return URL地址
     * @throws Exception
     */
    String presignedGetObjectUrl(String objectName) throws Exception;
    /**
     * 删除文件
     * @param objectName 需要删除的对象名称（文件名称）
     * @throws Exception
     */
    void deleteObject(String objectName) throws Exception;
}
