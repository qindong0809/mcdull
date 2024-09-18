package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileFolderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * url上传文件
 *
 */
@Data
public class FileUrlUploadDTO implements DTO {

    @SchemaEnum(value = FileFolderTypeEnum.class, desc = "业务类型")
    private Integer folder;

    @Schema(description = "文件url")
    @NotBlank(message = "文件url不能为空")
    private String fileUrl;

}
