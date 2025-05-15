package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class MessageAudit implements Audit {

    @AuditDescription(label = "是否已读", sort = 1)
    private String readFlag;

}
