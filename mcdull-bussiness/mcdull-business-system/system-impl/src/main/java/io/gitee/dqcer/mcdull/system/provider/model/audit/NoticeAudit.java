package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

@Data
public class NoticeAudit implements Audit {

    /**
     * 1公告 2动态
     */
    @AuditDescription(label = "类型", sort = 1)
    private Integer noticeTypeName;

    @AuditDescription(label = "标题", sort = 2)
    private String title;

    @AuditDescription(label = "是否全部可见", sort = 3)
    private String allVisibleFlag;

    @AuditDescription(label = "是否定时发布", sort = 4)
    private String scheduledPublishFlag;

    @AuditDescription(label = "发布时间", sort = 5)
    private String publishTime;

    @AuditDescription(label = "纯文本内容", sort = 6)
    private String contentText;

    @AuditDescription(label = "html内容", sort = 7)
    private String contentHtml;

    @AuditDescription(label = "附件", sort = 8)
    private String attachment;

    @AuditDescription(label = "来源", sort = 9)
    private String source;

    @AuditDescription(label = "作者", sort = 10)
    private String author;

    @AuditDescription(label = "文号", sort = 11)
    private String documentNumber;

}
