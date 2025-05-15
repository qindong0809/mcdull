package io.gitee.dqcer.mcdull.system.provider.model.dto.workflow;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlowCategoryListDTO implements DTO {

    @Schema(description = "父流程分类id")
    private Long parentId;

    @Schema(description = "流程分类名称")
    private String categoryName;

    @Schema(description = "显示顺序")
    private Long orderNum;

}
