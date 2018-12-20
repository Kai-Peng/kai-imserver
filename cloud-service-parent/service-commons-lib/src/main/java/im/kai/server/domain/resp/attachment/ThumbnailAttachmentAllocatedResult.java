package im.kai.server.domain.resp.attachment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 带缩略图的附件分配结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThumbnailAttachmentAllocatedResult {
    /**
     * 缩略图
     */
    private AttachmentAllocatedResult thumbnail;
    /**
     * 原文件
     */
    private AttachmentAllocatedResult file;
}
