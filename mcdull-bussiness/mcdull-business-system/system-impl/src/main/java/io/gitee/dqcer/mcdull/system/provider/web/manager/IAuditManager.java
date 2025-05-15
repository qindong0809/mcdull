package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;

/**
 * Audit service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface IAuditManager {

    /**
     * Save by add enum （通过添加枚举保存）
     *
     * @param bizIndex    业务指数
     * @param bizId       业务 ID
     * @param auditBean   审计 Bean
     */
    <T extends Audit> void saveByAddEnum(String bizIndex, Integer bizId, T auditBean);

    /**
     * Save by update 枚举
     *
     * @param bizIndex     业务指数
     * @param bizId        bizId
     * @param oldAuditBean oldAuditBean
     * @param newAuditBean newAuditBean
     */
    <T extends Audit> void saveByUpdateEnum(String bizIndex, Integer bizId, T oldAuditBean, T newAuditBean);

    /**
     * Save by delete 枚举
     *
     * @param bizIndex  业务指数
     * @param bizId     bizId
     * @param reason reason
     */
    void saveByDeleteEnum(String bizIndex, Integer bizId, String reason);

    /**
     * Save by status 枚举
     *
     * @param bizIndex bizIndex
     * @param bizId    业务 ID
     * @param active   active
     * @param reason   原因
     */
    void saveByStatusEnum(String bizIndex, Integer bizId, boolean active, String reason);
}
