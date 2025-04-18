package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class FeedbackAudit implements Audit {

    @AuditDescription(label = "反馈编码", sort = 1)
    private String code;

    @AuditDescription(label = "反馈内容", sort = 2)
    private String feedbackContent;

    @AuditDescription(label = "反馈附件", sort = 3)
    private String feedbackAttachment;

    @AuditDescription(label = "反馈人", sort = 4)
    private String userName;
}
