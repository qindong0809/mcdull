package io.gitee.dqcer.mcdull.uac.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class UserAudit implements Audit {

    @AuditDescription(label = "账号", sort = 1)
    private String loginName;

    @AuditDescription(label = "姓名", sort = 2)
    private String actualName;

    @AuditDescription(label = "邮箱", sort = 3)
    private String email;

    @AuditDescription(label = "性别", sort = 4)
    private String gender;

    @AuditDescription(label = "手机号", sort = 5)
    private String phone;

    @AuditDescription(label = "部门", sort = 6)
    private String department;

    @AuditDescription(label = "角色", sort = 6)
    private String roleJoin;

    @AuditDescription(label = "备注", sort = 7)
    private String remark;
}
