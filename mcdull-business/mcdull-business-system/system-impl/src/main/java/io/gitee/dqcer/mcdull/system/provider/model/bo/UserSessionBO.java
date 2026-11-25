package io.gitee.dqcer.mcdull.system.provider.model.bo;

import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.support.BO;
import io.gitee.dqcer.mcdull.system.facade.model.enums.GenderEnum;
import io.gitee.dqcer.mcdull.system.provider.model.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户会话快照：登录态相关的展示数据（含登录来源 ip、user-agent、用户类型）。
 * <p>
 * 本对象不是 UserEntity 的另一种表示，而是「会话视角」的只读快照，
 * 因此字段集合与用户本体不同（如 ip / userAgent / userType 不属于用户实体）。
 *
 * @author dqcer
 * @since 2024/05/20
 */
@Setter
@Getter
@ToString
public class UserSessionBO implements BO {

    @Schema(description = "员工id")
    private Integer employeeId;

    @SchemaEnum(UserTypeEnum.class)
    private UserTypeEnum userType;

    @Schema(description = "登录账号")
    private String loginName;

    @Schema(description = "员工名称")
    private String actualName;

    @SchemaEnum(GenderEnum.class)
    private Integer gender;

    @Schema(description = "性别名称")
    private String genderName;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "部门id")
    private Integer departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "是否为超管")
    private Boolean administratorFlag;

    @Schema(description = "请求ip")
    private String ip;

    @Schema(description = "请求user-agent")
    private String userAgent;
}
