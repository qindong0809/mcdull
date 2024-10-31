package io.gitee.dqcer.mcdull.uac.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class ConfigAudit implements Audit {

    @AuditDescription(label = "配置名称", sort = 1)
    private String configName;

    @AuditDescription(label = "配置key", sort = 2)
    private String configKey;

    @AuditDescription(label = "配置值", sort = 3)
    private String configValue;

    @AuditDescription(label = "备注", sort = 4)
    private String remark;
}
