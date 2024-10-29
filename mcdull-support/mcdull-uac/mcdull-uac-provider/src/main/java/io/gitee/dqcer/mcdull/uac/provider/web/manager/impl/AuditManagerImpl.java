package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditUtil;
import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IBizAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class AuditManagerImpl implements IAuditManager {


    @Resource
    private IBizAuditService bizAuditService;


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
        UnifySession<?> session = UserContextHolder.getSession();
        if (ObjUtil.isNull(session)) {
            return;
        }
        String bizTypeCode = session.getPermissionCode();
        String comment = StrUtil.EMPTY;
        if (typeEnum == OperationTypeEnum.UPDATE) {
            comment = AuditUtil.compareStr(oldAuditBean, newAuditBean);
        } else if (typeEnum == OperationTypeEnum.ADD) {
            comment = AuditUtil.compareStr(oldAuditBean);
        } else if (typeEnum == OperationTypeEnum.DELETE
                || typeEnum == OperationTypeEnum.DISABLE
                || typeEnum == OperationTypeEnum.ENABLE) {
            comment = reason;
        }
        String loginName = session.getLoginName();
        bizAuditService.insert(bizTypeCode, typeEnum, bizIndex, bizId, comment, loginName, new Date(), null);
    }

}
