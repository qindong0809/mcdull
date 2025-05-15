package io.gitee.dqcer.mcdull.system.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.system.provider.model.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 员工信息
 *
 */
@Data
public class UserAllVO {

    @Schema(description = "主键id")
    private Integer employeeId;

    @Schema(description = "登录账号")
    private String loginName;

    @SchemaEnum(GenderEnum.class)
    private Integer gender;

    @Schema(description = "员工名称")
    private String actualName;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "部门id")
    private Integer departmentId;

    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;

    @Schema(description = "是否 超级管理员")
    private Boolean administratorFlag;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "创建时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @Schema(description = "角色列表")
    private List<Integer> roleIdList;

    @Schema(description = "角色名称列表")
    private List<String> roleNameList;
}
