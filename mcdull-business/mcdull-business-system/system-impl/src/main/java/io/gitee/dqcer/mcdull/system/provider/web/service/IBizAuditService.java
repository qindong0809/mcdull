package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.common.audit.Audit;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.OperationTypeEnum;

import java.util.Date;

/**
 * 业务审计写入契约。
 *
 * <p>查询与导出能力见 {@link IBizAuditQueryService}。
 */
public interface IBizAuditService extends IRepository<BizAuditEntity> {

    Integer insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId,
                   String comment, String operator, Date operationTime, String ext);

    <T extends Audit> void saveByAddEnum(String bizIndex, Integer bizId, T auditBean);

    <T extends Audit> void saveByUpdateEnum(String bizIndex, Integer bizId, T oldAuditBean, T newAuditBean);

    void saveByDeleteEnum(String bizIndex, Integer bizId, String reason);

    void saveByStatusEnum(String bizIndex, Integer bizId, boolean active, String reason);
}
