package im.kai.server.domain.resp.attachment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 附件上传请求的分配结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentAllocatedResult {
    /**
     *对象id
     */
    private String id;
    /**
     * 附件对象的上传地址
     */
    private String putUrl;
    /**
     * 附件对象的下载地址
     */
    private String getUrl;
}
