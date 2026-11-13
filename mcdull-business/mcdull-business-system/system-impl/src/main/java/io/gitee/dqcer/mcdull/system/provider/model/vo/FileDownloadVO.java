package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件下载
 *
 * @author dqcer
 */
@Data
public class FileDownloadVO implements VO {

    @Schema(description = "文件字节数据")
    private byte[] data;

    @Schema(description = "文件元数据")
    private FileMetadataVO metadata;


}
