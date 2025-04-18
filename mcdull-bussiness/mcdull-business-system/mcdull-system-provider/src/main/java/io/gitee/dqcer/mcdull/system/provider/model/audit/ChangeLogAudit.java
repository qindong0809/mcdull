package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class ChangeLogAudit implements Audit {

    @AuditDescription(label = "版本号", sort = 1)
    private String version;

    @AuditDescription(label = "类型", sort = 2)
    private String type;

    @AuditDescription(label = "发布人", sort = 3)
    private String publishAuthor;

    @AuditDescription(label = "发布日期", sort = 4)
    private String publicDate;

    @AuditDescription(label = "发布内容", sort = 5)
    private String content;

    @AuditDescription(label = "发布链接", sort = 6)
    private String link;
}
