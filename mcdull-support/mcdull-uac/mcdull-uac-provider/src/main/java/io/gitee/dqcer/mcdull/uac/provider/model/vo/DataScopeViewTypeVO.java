package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 数据范围
 *
 * @author dqcer
 * @since 2024/8/19 13:10
 */

@Data
@Builder
public class DataScopeViewTypeVO implements VO {

    @Schema(description = "可见范围")
    private Integer viewType;

    @Schema(description = "可见范围名称")
    private String viewTypeName;

    @Schema(description = "级别,用于表示范围大小")
    private Integer viewTypeLevel;
}
