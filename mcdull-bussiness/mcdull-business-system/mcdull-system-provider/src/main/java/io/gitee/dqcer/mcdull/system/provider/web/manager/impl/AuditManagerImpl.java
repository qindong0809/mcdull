package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditUtil;
import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditFieldEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IBizAuditFieldRepository;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IBizAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AuditManagerImpl implements IAuditManager {


    @Resource
    private IBizAuditService bizAuditService;

    @Resource
    private IBizAuditFieldRepository bizAuditFieldRepository;


    @Override
    public <T extends Audit> void saveByAddEnum(String bizIndex, Integer bizId, T auditBean) {
        this.common(OperationTypeEnum.ADD,  bizIndex, bizId, null, auditBean, null);
    }
    @Override
    public <T extends Audit> void saveByUpdateEnum(String bizIndex, Integer bizId, T oldAuditBean, T newAuditBean) {
        this.common(OperationTypeEnum.UPDATE,  bizIndex, bizId, oldAuditBean, newAuditBean, null);
    }

    @Override
    public void saveByDeleteEnum(String bizIndex, Integer bizId, String reason) {
        this.common(OperationTypeEnum.DELETE,  bizIndex, bizId, null, null, reason);
    }

    @Override
    public void saveByStatusEnum(String bizIndex, Integer bizId, boolean active, String reason) {
        this.common(active ? OperationTypeEnum.ENABLE : OperationTypeEnum.DISABLE,  bizIndex, bizId, null, null, reason);
    }


    private <T extends Audit> void common(OperationTypeEnum typeEnum, String bizIndex, Integer bizId,
                                          T oldAuditBean, T newAuditBean, String reason) {
        if (ObjUtil.isNull(bizId) || StrUtil.isBlank(bizIndex)) {
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
        Integer bizAuditId = bizAuditService.insert(bizTypeCode, typeEnum, bizIndex, bizId, comment, loginName, new Date(), null);
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
        bizAuditFieldRepository.saveBatch(list, list.size());
    }
}
