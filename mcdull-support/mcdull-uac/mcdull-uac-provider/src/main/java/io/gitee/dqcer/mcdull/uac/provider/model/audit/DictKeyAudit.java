package io.gitee.dqcer.mcdull.uac.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class DictKeyAudit implements Audit {

    @AuditDescription(label = "字典编码", sort = 1)
    private String keyCode;

    @AuditDescription(label = "字典名称", sort = 2)
    private String keyName;

    @AuditDescription(label = "备注", sort = 3)
    private String remark;
}
