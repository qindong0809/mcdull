package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 分页查询 系统配置
 *
 * @author dqcer
 * @since 2024/04/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigQueryDTO extends PagedDTO {

    @Schema(description = "参数KEY")
    @Length(max = 50, message = "参数Key最多50字符")
    private String configKey;

    @Schema(description = "附件名称")
    @Length(max = 50, message = "附件名称最多50字符")
    private String attachmentName;
}
