package io.gitee.dqcer.mcdull.uac.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

/**
 * 帮助文档目录审核
 *
 * @author dqcer
 * @since 2024/11/04
 */
@Data
public class HelpDocCatalogAudit implements Audit {

    @AuditDescription(label = "名称", sort = 1)
    private String name;

    @AuditDescription(label = "排序", sort = 2)
    private Integer sort;

    @AuditDescription(label = "父级名称", sort = 3)
    private String parentName;
}
