package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

/**
 * 帮助文档
 *
 * @author dqcer
 * @since 2024/11/04
 */
@Data
public class HelpDocAudit implements Audit {

    @AuditDescription(label = "所属分类", sort = 1)
    private String helpDocCatalogName;

    @AuditDescription(label = "标题", sort = 2)
    private String title;

    @AuditDescription(label = "内容", sort = 3)
    private String contentText;

    @AuditDescription(label = "内容html", sort = 4)
    private String contentHtml;

    @AuditDescription(label = "附件", sort = 5)
    private String attachment;

    private Integer sort;
}
