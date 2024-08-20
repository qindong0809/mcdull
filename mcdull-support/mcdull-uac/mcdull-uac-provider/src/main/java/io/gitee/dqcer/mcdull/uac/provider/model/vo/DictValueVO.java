package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典价值视图对象
 * dict value
 *
 * @author dqcer
 * @since 2024/04/28
 */
@Data
public class DictValueVO implements VO {

    @Schema(description = "字典值id")
    private Integer dictValueId;

    @Schema(description = "字典id")
    private Integer dictKeyId;

    @Schema(description = "字典编码")
    private String valueCode;

    @Schema(description = "字典名称")
    private String valueName;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "描述")
    private String remark;
}