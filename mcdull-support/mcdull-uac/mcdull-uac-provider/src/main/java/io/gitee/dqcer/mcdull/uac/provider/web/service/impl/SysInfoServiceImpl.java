package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.EmailConfigBO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EmailConfigDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SysInfoEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EmailConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ISysInfoRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISysInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Sys info service impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SysInfoServiceImpl
        extends BasicServiceImpl<ISysInfoRepository> implements ISysInfoService {

    @Override
    public EmailConfigBO getEmailConfig() {
        SysInfoEntity entity = this.getOne();
        if (ObjUtil.isNull(entity)) {
            return null;
        }
        EmailConfigBO bo = new EmailConfigBO();
        bo.setHost(entity.getEmailHost());
        bo.setPort(entity.getEmailPort());
        bo.setUsername(entity.getEmailUsername());
        bo.setPassword(entity.getEmailPassword());
        bo.setFrom(entity.getEmailFrom());
        return bo;
    }

    @Override
    public EmailConfigVO detail() {
        EmailConfigVO vo = new EmailConfigVO();
        SysInfoEntity entity = this.getOne();
        if (ObjUtil.isNull(entity)) {
            return null;
        }
        vo.setEmailHost(entity.getEmailHost());
        vo.setEmailPort(entity.getEmailPort());
        vo.setEmailUsername(entity.getEmailUsername());
        vo.setEmailPassword(entity.getEmailPassword());
        vo.setEmailFrom(entity.getEmailFrom());
        return vo;
    }

    private SysInfoEntity getOne() {
        List<SysInfoEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        LogHelp.warn(log, "邮箱配置为空");
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(EmailConfigDTO dto) {
        SysInfoEntity entity = this.getOne();
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(dto.getEmailUsername());
        }
        entity.setEmailHost(dto.getEmailHost());
        entity.setEmailPort(dto.getEmailPort());
        entity.setEmailUsername(dto.getEmailUsername());
        entity.setEmailPassword(dto.getEmailPassword());
        entity.setEmailFrom(dto.getEmailFrom());
        baseRepository.updateById(entity);
    }
}
