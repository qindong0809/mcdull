package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录失败 列表VO
 *
 */

@Data
public class LoginLockedVO implements VO {

    private Integer loginFailId;


    @Schema(description = "登录名")
    private String loginName;

    @Schema(description = "连续登录失败次数")
    private Integer loginFailCount;

    @Schema(description = "锁定状态:1锁定，0未锁定")
    private Boolean lockFlag;

    @Schema(description = "连续登录失败锁定开始时间")
    private LocalDateTime loginLockBeginTime;

    @Schema(description = "连续登录失败锁定结束时间")
    private LocalDateTime loginLockEndTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}