package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 用户视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
@ToString
public class UserVO implements VO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Integer employeeId;

    @Schema(description = "登录账号")
    private String loginName;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "部门id")
    private Integer departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "角色列表")
    private List<Integer> roleIdList;

    @Schema(description = "角色名称列表")
    private List<String> roleNameList;

    @Schema(description = "是否是超级管理员")
    private Boolean administratorFlag;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人")
    private Integer createdBy;

    @Schema(description = "创建人名称")
    private String createdByName;

    @Schema(description = "更新人")
    private Integer updatedBy;

    @Schema(description = "更新人名称")
    private String updatedByName;

    @Schema(description = "创建时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    protected Date createdTime;

    @Schema(description = "更新时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    protected Date updatedTime;

}
