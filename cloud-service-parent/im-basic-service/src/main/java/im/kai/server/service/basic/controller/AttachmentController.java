package im.kai.server.service.basic.controller;

import im.kai.server.domain.ApiResult;
import im.kai.server.domain.resp.attachment.AttachmentAllocatedResult;
import im.kai.server.domain.resp.attachment.AvatarAttachmentAllocatedResult;
import im.kai.server.domain.resp.attachment.ThumbnailAttachmentAllocatedResult;
import im.kai.server.exception.NotFoundException;
import im.kai.server.exception.ServerErrorException;
import im.kai.server.service.basic.cloudstorage.UrlSigner;
import im.kai.server.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件的云存储服务
 */
@Slf4j
@RestController
@RequestMapping("/v1/attachments")
public class AttachmentController {

    @Resource
    private UrlSigner avatarUrlSigner;
    @Resource
    private UrlSigner imageUrlSigner;
    @Resource
    private UrlSigner videoUrlSigner;
    @Resource
    private UrlSigner fileUrlSigner;
    @Resource
    private UrlSigner audioUrlSigner;

    /**
     * 分配头像的云存储地址
     * @return
     */
    @GetMapping("/avatar/{objectName}")
    public void allocateAvatarDownloadUrl(
            @PathVariable(name = "objectName") String objectName,
            HttpServletResponse response) throws IOException {
        if(Utils.isEmpty(objectName)){
            throw new NotFoundException();
        }
        ApiResult apiResult = allocateDownloadUrl(avatarUrlSigner, objectName, "头像");
        response.sendRedirect((String)apiResult.getData());
    }

    /**
     * 分配头像的云存储地址
     * @return
     * @throws Exception
     */
    @PutMapping("/avatar")
    public ApiResult allocateAvatarUploadUrl() {
        try {
            String objectName = generateObjectName();
            String putObjectUrl = avatarUrlSigner.presignedPutObjectUrl(objectName);
            return ApiResult.success(new AvatarAttachmentAllocatedResult(objectName, putObjectUrl));
        }catch(Exception ex){
            throw new ServerErrorException("分配头像上传地址时出错", ex);
        }
    }
    /**
     * 分配图像的云存储地址
     * @return
     * @throws Exception
     */
    @PutMapping({"/image", "/image/{count}"})
    public ApiResult allocateImageUploadUrl(@PathVariable(value = "count", required = false) Integer count) {
        int n = count == null ? 1 : count;
        n = Math.min(9, Math.max(1, n));
        List<ThumbnailAttachmentAllocatedResult> results = new ArrayList<>(n);
        for(int i=0; i<n; i++){
            results.add(allocateThumbnailAttachmentUrls(imageUrlSigner, imageUrlSigner, "图片"));
        }
        return ApiResult.success(results);
    }

    /**
     * 分配视频的云存储地址
     * @return
     * @throws Exception
     */
    @PutMapping("/video")
    public ApiResult allocateVideoUploadUrl() {
        return ApiResult.success(allocateThumbnailAttachmentUrls(imageUrlSigner, videoUrlSigner, "视频"));
    }
    /**
     * 分配音频的云存储地址
     * @return
     * @throws Exception
     */
    @PutMapping("/audio")
    public ApiResult allocateAudioUploadUrl() {
        return ApiResult.success(allocateAttachmentUrl(audioUrlSigner, "音频"));
    }
    /**
     * 分配文件的云存储地址
     * @return
     * @throws Exception
     */
    @PutMapping("/file")
    public ApiResult allocateFileUploadUrl() {
        return ApiResult.success(allocateAttachmentUrl(fileUrlSigner, "文件"));
    }
    /**
     * 分配附件的云存储地址
     * @param urlSigner
     * @param objectName
     * @param type
     * @return
     * @throws Exception
     */
    private ApiResult allocateDownloadUrl(UrlSigner urlSigner, String objectName, String type) {
        try {
            String getObjectUrl = urlSigner.presignedGetObjectUrl(objectName);
            return ApiResult.success(getObjectUrl);
        }catch(Exception ex){
            throw new ServerErrorException("分配" + type + "下载地址时出错", ex);
        }
    }

    /**
     * 分配带缩略图的附件云存储地址
     * @param thumbnailUrlSigner
     * @param fileUrlSigner
     * @param type
     * @return
     */
    private ThumbnailAttachmentAllocatedResult allocateThumbnailAttachmentUrls(
            UrlSigner thumbnailUrlSigner, UrlSigner fileUrlSigner, String type){
        AttachmentAllocatedResult thumbnail = allocateAttachmentUrl(thumbnailUrlSigner, type);
        AttachmentAllocatedResult file = allocateAttachmentUrl(fileUrlSigner, type);
        return new ThumbnailAttachmentAllocatedResult(thumbnail, file);
    }
    /**
     * 分配附件的云存储地址
     * @param urlSigner
     * @param type
     * @return
     * @throws Exception
     */
    private AttachmentAllocatedResult allocateAttachmentUrl(UrlSigner urlSigner, String type) {
        try {
            String objectName = generateObjectName();
            String putObjectUrl = urlSigner.presignedPutObjectUrl(objectName);
            String getObjectUrl = urlSigner.presignedGetObjectUrl(objectName);
            return new AttachmentAllocatedResult(objectName, putObjectUrl, getObjectUrl);
        }catch(Exception ex){
            throw new ServerErrorException("分配" + type + "云存储地址时出错", ex);
        }
    }
    /**
     * 生成存储地址
     * @return
     */
    private String generateObjectName(){
        byte[] name = new byte[16];
        new SecureRandom().nextBytes(name);
        return String.valueOf(System.currentTimeMillis()) +
                "-" +
                Base64.encodeBase64URLSafeString(name);
    }
}
