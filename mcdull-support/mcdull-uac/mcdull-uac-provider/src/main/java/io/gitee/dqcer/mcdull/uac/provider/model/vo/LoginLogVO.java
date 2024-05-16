package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.swagger.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 登录日志
 *
 */
@Data
public class LoginLogVO {

    private Integer loginLogId;

    @Schema(description = "用户id")
    private Integer userId;

    @SchemaEnum(value = UserTypeEnum.class, desc = "用户类型")
    private Integer userType;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "登录名")
    private String loginName;

    @Schema(description = "登录ip")
    private String loginIp;

    @Schema(description = "登录ip地区")
    private String loginIpRegion;

    @Schema(description = "user-agent")
    private String userAgent;

    @Schema(description = "remark")
    private String remark;

    @SchemaEnum(LoginLogResultTypeEnum.class)
    private Integer loginResult;

    private LocalDateTime createTime;

}
