package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件信息
 *
 */
@Data
public class FileUploadVO {

    @Schema(description = "文件id")
    private Integer fileId;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "fileUrl")
    private String fileUrl;

    @Schema(description = "fileKey")
    private String fileKey;

    @Schema(description = "文件大小")
    private Integer fileSize;

    @Schema(description = "文件类型")
    private String fileType;
}
