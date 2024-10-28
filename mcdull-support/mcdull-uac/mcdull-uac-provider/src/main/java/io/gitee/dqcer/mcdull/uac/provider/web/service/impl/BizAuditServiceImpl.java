package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IBizAuditRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IBizAuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Biz Audit Service
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class BizAuditServiceImpl
        extends BasicServiceImpl<IBizAuditRepository> implements IBizAuditService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId,
                       String comment, String operator, Date operationTime, String ext) {
        BizAuditEntity entity = new BizAuditEntity();
        entity.setBizTypeCode(bizTypeCode);
        entity.setComment(comment);
        entity.setOperationTime(operationTime);
        entity.setOperator(operator);
        entity.setOperation(operationTypeEnum.getCode());
        entity.setBizId(bizId);
        entity.setBizIndex(bizIndex);
        entity.setExt(ext);
        baseRepository.save(entity);
    }
}
