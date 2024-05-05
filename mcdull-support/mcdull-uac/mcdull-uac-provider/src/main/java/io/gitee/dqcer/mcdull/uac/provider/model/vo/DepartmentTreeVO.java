package io.gitee.dqcer.mcdull.uac.provider.model.vo;

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
public class DepartmentTreeVO extends DepartmentVO {

    @Schema(description = "同级上一个元素id")
    private Integer preId;

    @Schema(description = "同级下一个元素id")
    private Integer nextId;

    @Schema(description = "子部门")
    private List<DepartmentTreeVO> children;

    @Schema(description = "自己和所有递归子部门的id集合")
    private List<Integer> selfAndAllChildrenIdList;

}
