package io.gitee.dqcer.mcdull.system.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

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

    @Schema(description = "部门负责人名称")
    private String managerName;

    @Schema(description = "上级部门id")
    private Integer parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @Schema(description = "更新时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;

}
