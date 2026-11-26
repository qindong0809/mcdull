package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TenantUserVO {
    private Integer userId;
    private String loginName;
    private String actualName;
    private Integer departmentId;
    private Boolean inactive;
    private Date joinedTime;
}
