package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.PasswordPolicyAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.PasswordPolicyDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.PasswordPolicyEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PasswordPolicyVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IPasswordPolicyRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IPasswordPolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;


/**
 * Password Policy Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class PasswordPolicyServiceImpl
        extends BasicServiceImpl<IPasswordPolicyRepository> implements IPasswordPolicyService {

    @Resource
    private IAuditManager auditManager;

    private static final String MODULE_NAME = "密码策略";

    @Override
    public PasswordPolicyVO detail() {
        PasswordPolicyEntity passwordPolicy = this.get();
        return this.convert(passwordPolicy);
    }

    private PasswordPolicyEntity get() {
        List<PasswordPolicyEntity> list = baseRepository.list();
        if (CollUtil.isEmpty(list)) {
            this.throwDataNotExistException("is empty");
        }
        return list.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PasswordPolicyDTO dto) {
        PasswordPolicyEntity entity = this.get();
        PasswordPolicyEntity oldEntity = ObjUtil.cloneByStream(entity);
        entity.setRepeatablePasswordNumber(dto.getRepeatablePasswordNumber());
        entity.setFailedLoginMaximumNumber(dto.getFailedLoginMaximumNumber());
        entity.setFailedLoginMaximumTime(dto.getFailedLoginMaximumTime());
        entity.setPasswordExpiredPeriod(dto.getPasswordExpiredPeriod());
        baseRepository.updateById(entity);
        auditManager.saveByUpdateEnum(MODULE_NAME, entity.getId(),
                this.buildAuditLog(oldEntity), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(PasswordPolicyEntity entity) {
        PasswordPolicyAudit audit = new PasswordPolicyAudit();
        audit.setRepeatablePasswordNumber(entity.getRepeatablePasswordNumber());
        audit.setFailedLoginMaximumNumber(entity.getFailedLoginMaximumNumber());
        audit.setFailedLoginMaximumTime(entity.getFailedLoginMaximumTime());
        audit.setPasswordExpiredPeriod(entity.getPasswordExpiredPeriod());
        return audit;
    }

    private PasswordPolicyVO convert(PasswordPolicyEntity entity) {
        PasswordPolicyVO vo = new PasswordPolicyVO();
        vo.setRepeatablePasswordNumber(entity.getRepeatablePasswordNumber());
        vo.setFailedLoginMaximumNumber(entity.getFailedLoginMaximumNumber());
        vo.setFailedLoginMaximumTime(entity.getFailedLoginMaximumTime());
        vo.setPasswordExpiredPeriod(entity.getPasswordExpiredPeriod());
        return vo;
    }
}
