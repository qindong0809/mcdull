package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.common.audit.Audit;
import io.gitee.dqcer.mcdull.common.audit.AuditUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditFieldEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.OperationTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.BizAuditMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IBizAuditFieldService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IBizAuditService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务审计写入服务。
 *
 * <p>只依赖 Mapper 与审计字段服务，不反向依赖 User / Menu / CommonManager，
 * 供所有 CRUD Service 注入写审计而不形成循环。
 */
@Service
public class BizAuditServiceImpl extends BasicCurdServiceImpl<BizAuditMapper, BizAuditEntity> implements IBizAuditService {

    @Resource
    private IBizAuditFieldService bizAuditFieldService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId,
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
        entity.setTraceId(UserContextHolder.getSession().getTraceId());
        super.save(entity);
        return entity.getId();
    }

    @Override
    public <T extends Audit> void saveByAddEnum(String bizIndex, Integer bizId, T auditBean) {
        this.common(OperationTypeEnum.ADD, bizIndex, bizId, null, auditBean, null);
    }

    @Override
    public <T extends Audit> void saveByUpdateEnum(String bizIndex, Integer bizId, T oldAuditBean, T newAuditBean) {
        this.common(OperationTypeEnum.UPDATE, bizIndex, bizId, oldAuditBean, newAuditBean, null);
    }

    @Override
    public void saveByDeleteEnum(String bizIndex, Integer bizId, String reason) {
        this.common(OperationTypeEnum.DELETE, bizIndex, bizId, null, null, reason);
    }

    @Override
    public void saveByStatusEnum(String bizIndex, Integer bizId, boolean active, String reason) {
        this.common(active ? OperationTypeEnum.ENABLE : OperationTypeEnum.DISABLE, bizIndex, bizId, null, null, reason);
    }

    private <T extends Audit> void common(OperationTypeEnum typeEnum, String bizIndex, Integer bizId,
                                          T oldAuditBean, T newAuditBean, String reason) {
        if (ObjectUtil.isNull(bizId) || CharSequenceUtil.isBlank(bizIndex)) {
            throw new IllegalArgumentException();
        }
        UnifySession session = UserContextHolder.getSession();
        if (ObjUtil.isNull(session)) {
            return;
        }
        String bizTypeCode = session.getPermissionCode();
        String comment = StrUtil.EMPTY;
        List<AuditUtil.FieldDiff> diffList = new ArrayList<>();
        if (typeEnum == OperationTypeEnum.UPDATE) {
            diffList = AuditUtil.compare(oldAuditBean, newAuditBean);
        } else if (typeEnum == OperationTypeEnum.ADD) {
            diffList = AuditUtil.compare(oldAuditBean);
        } else if (typeEnum == OperationTypeEnum.DELETE
                || typeEnum == OperationTypeEnum.DISABLE
                || typeEnum == OperationTypeEnum.ENABLE) {
            comment = reason;
        }
        String loginName = session.getLoginName();
        Integer bizAuditId = this.insert(bizTypeCode, typeEnum, bizIndex, bizId, comment, loginName, new Date(), null);
        if (CollUtil.isNotEmpty(diffList)) {
            this.insertAuditFieldList(diffList, bizAuditId);
        }
    }

    private void insertAuditFieldList(List<AuditUtil.FieldDiff> diffList, Integer bizAuditId) {
        List<BizAuditFieldEntity> list = new ArrayList<>();
        for (AuditUtil.FieldDiff fieldDiff : diffList) {
            BizAuditFieldEntity auditField = new BizAuditFieldEntity();
            auditField.setBizAuditId(bizAuditId);
            auditField.setFieldName(fieldDiff.getFieldName());
            auditField.setOldValue(fieldDiff.getBeforeValue());
            auditField.setNewValue(fieldDiff.getAfterValue());
            auditField.setSortOrder(fieldDiff.getSortOrder());
            list.add(auditField);
        }
        bizAuditFieldService.saveBatch(list, list.size());
    }
}
