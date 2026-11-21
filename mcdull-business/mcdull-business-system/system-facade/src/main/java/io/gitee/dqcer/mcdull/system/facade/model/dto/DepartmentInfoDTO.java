package io.gitee.dqcer.mcdull.system.facade.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 部门信息传输对象（服务间共享，不绑定展示格式）
 */
@Getter
@Setter
@ToString
public class DepartmentInfoDTO implements VO {

    @Schema(description = "部门id")
    private Integer departmentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门负责人id")
    private Integer managerId;

    @Schema(description = "部门负责人名称")
    private String managerName;

    @Schema(description = "上级部门id")
    private Integer parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

}
