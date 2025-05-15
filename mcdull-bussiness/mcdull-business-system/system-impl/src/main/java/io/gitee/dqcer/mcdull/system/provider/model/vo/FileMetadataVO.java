package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件元数据
 *
 */
@Data
public class FileMetadataVO implements VO {

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件大小/字节")
    private Integer fileSize;

    @Schema(description = "文件格式")
    private String fileFormat;
}
