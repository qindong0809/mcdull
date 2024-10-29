package io.gitee.dqcer.mcdull.uac.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class RoleAudit implements Audit {

    @AuditDescription(label = "角色名称", sort = 1)
    private String roleName;

    @AuditDescription(label = "角色编码", sort = 2)
    private String roleCode;

    @AuditDescription(label = "备注", sort = 3)
    private String remark;
}
