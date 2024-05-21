package io.gitee.dqcer.mcdull.uac.provider.model.bo;

import io.gitee.dqcer.mcdull.framework.base.support.BO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 数据范围
 *
 * @author dqcer
 * @since 2024/05/20
 */
@Data
@Builder
public class DataScopeBO implements BO {

    @Schema(description = "数据范围类型")
    private Integer dataScopeType;

    @Schema(description = "数据范围名称")
    private String dataScopeTypeName;

    @Schema(description = "描述")
    private String dataScopeTypeDesc;

    @Schema(description = "顺序")
    private Integer dataScopeTypeSort;

}
