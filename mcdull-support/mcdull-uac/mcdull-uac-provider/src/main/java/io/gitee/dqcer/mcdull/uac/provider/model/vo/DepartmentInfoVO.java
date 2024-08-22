package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Getter
@Setter
@ToString
public class DepartmentInfoVO implements VO {

    @Schema(description = "部门id")
    private Integer departmentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门负责人id")
    private Integer managerId;

    @Schema(description = "上级部门id")
    private Integer parentId;

    @Schema(description = "排序")
    private Integer sort;

}
