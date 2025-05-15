package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

/**
 * 文件夹审核
 *
 * @author dqcer
 * @since 2024/11/29
 */
@Data
public class FolderAudit implements Audit {

    @AuditDescription(label = "名称", sort = 1)
    private String name;

    @AuditDescription(label = "上级", sort = 2)
    private String parentName;

    @AuditDescription(label = "排序", sort = 3)
    private Integer sort;
}
