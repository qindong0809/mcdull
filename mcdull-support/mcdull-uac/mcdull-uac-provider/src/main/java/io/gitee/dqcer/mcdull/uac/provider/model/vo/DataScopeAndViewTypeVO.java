package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * data scope and view type vo
 *
 * @author dqcer
 * @since 2024/8/19 13:10
 */

@Data
public class DataScopeAndViewTypeVO implements VO {

    @Schema(description = "数据范围类型")
    private Integer dataScopeType;

    @Schema(description = "数据范围名称")
    private String dataScopeTypeName;

    @Schema(description = "描述")
    private String dataScopeTypeDesc;

    @Schema(description = "顺序")
    private Integer dataScopeTypeSort;

    @Schema(description = "可见范围列表")
    private List<DataScopeViewTypeVO> viewTypeList;

}
