package io.gitee.dqcer.mcdull.uac.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class DepartmentAudit implements Audit {

    @AuditDescription(label = "部门名称", sort = 1)
    private String name;

    @AuditDescription(label = "部门负责人", sort = 2)
    private String managerName;

    @AuditDescription(label = "上级部门", sort = 3)
    private String parentName;

    @AuditDescription(label = "排序", sort = 4)
    private Integer sort;
}
