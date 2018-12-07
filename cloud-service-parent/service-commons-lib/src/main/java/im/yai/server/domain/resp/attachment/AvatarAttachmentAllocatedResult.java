package im.kai.server.domain.resp.attachment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 头像上传请求的分配结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarAttachmentAllocatedResult {
    /**
     *对象id
     */
    private String id;
    /**
     * 头像对象的上传地址
     */
    private String putUrl;
}
