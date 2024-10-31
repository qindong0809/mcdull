package io.gitee.dqcer.mcdull.uac.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class DictValueAudit implements Audit {

    @AuditDescription(label = "字典值编码", sort = 1)
    private String valueCode;

    @AuditDescription(label = "字典值名称", sort = 2)
    private String valueName;

    @AuditDescription(label = "所属字典名称", sort = 3)
    private String keyName;

    @AuditDescription(label = "备注", sort = 4)
    private String remark;

    @AuditDescription(label = "排序", sort = 5)
    private Integer sort;
}
