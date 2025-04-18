package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class FormAudit implements Audit {

    @AuditDescription(label = "表单名称")
    private String name;

    @AuditDescription(label = "表单配置")
    private String jsonText;

    @AuditDescription(label = "是否发布")
    private Boolean publish;

    @AuditDescription(label = "备注")
    private String remark;
}
