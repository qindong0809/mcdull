package io.gitee.dqcer.mcdull.uac.provider.web.manager;

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
}
