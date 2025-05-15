package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 部门
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentTreeInfoVO extends DepartmentInfoVO {

    @Schema(description = "同级上一个元素id")
    private Integer preId;

    @Schema(description = "同级下一个元素id")
    private Integer nextId;

    @Schema(description = "子部门")
    private List<DepartmentTreeInfoVO> children;

    @Schema(description = "自己和所有递归子部门的id集合")
    private List<Integer> selfAndAllChildrenIdList;

}
