package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 帮助文档 关联项目
 *
 */
@Data
public class HelpDocRelationVO implements VO  {

    @Schema(description = "关联名称")
    private String relationName;

    @Schema(description = "关联id")
    private Integer relationId;
}
