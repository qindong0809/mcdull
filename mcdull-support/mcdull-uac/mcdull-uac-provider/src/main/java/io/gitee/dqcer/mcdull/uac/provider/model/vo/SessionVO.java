package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统更新日志 列表VO
 *
 */

@Data
public class SessionVO {

    private String id;

    private Integer loginId;

    private String loginName;

    private String actualName;

    private LocalDateTime createTime;

}