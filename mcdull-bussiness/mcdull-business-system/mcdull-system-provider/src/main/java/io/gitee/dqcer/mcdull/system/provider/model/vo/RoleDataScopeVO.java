package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色的数据范围
 *
 */
@Data
public class RoleDataScopeVO implements VO {

    @Schema(description = "数据范围id")
    private Integer dataScopeType;

    @Schema(description = "可见范围")
    private Integer viewType;
}
