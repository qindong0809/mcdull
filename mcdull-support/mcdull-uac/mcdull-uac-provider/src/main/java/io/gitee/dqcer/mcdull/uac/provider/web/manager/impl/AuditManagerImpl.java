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
        if (ObjUtil.isNull(auditBean) || ObjUtil.isNull(bizId) || StrUtil.isBlank(bizIndex)) {
            throw new IllegalArgumentException();
        }
        UnifySession<?> session = UserContextHolder.getSession();
        if (ObjUtil.isNull(session)) {
            return;
        }
        String bizTypeCode = session.getPermissionCode();
        String comment = AuditUtil.compareStr(auditBean);
        String loginName = session.getLoginName();
        bizAuditService.insert(bizTypeCode, OperationTypeEnum.ADD, bizIndex, bizId, comment, loginName, new Date(), null);
    }
}
