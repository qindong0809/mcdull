package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class PasswordPolicyAudit implements Audit {

    @AuditDescription(label = "密码连续错误次数", sort = 1)
    private Integer repeatablePasswordNumber;

    @AuditDescription(label = "密码错误最大次数", sort = 2)
    private Integer failedLoginMaximumNumber;

    @AuditDescription(label = "密码错误最大时间", sort = 3)
    private Integer failedLoginMaximumTime;

    @AuditDescription(label = "密码过期时间", sort = 4)
    private Integer passwordExpiredPeriod;
}
