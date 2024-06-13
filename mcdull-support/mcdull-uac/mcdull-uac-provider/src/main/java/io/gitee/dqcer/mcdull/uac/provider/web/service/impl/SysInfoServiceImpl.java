package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

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
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SysInfoServiceImpl extends BasicServiceImpl<ISysInfoRepository> implements ISysInfoService {


    @Override
    public EmailConfigBO getEmailConfig() {
        List<SysInfoEntity> list = baseRepository.list();
        if (list.size() > 0) {
            SysInfoEntity entity = list.get(0);
            EmailConfigBO emailConfigBO = new EmailConfigBO();
            emailConfigBO.setHost(entity.getEmailHost());
            emailConfigBO.setPort(entity.getEmailPort());
            emailConfigBO.setUsername(entity.getEmailUsername());
            emailConfigBO.setPassword(entity.getEmailPassword());
            emailConfigBO.setFrom(entity.getEmailFrom());
            return emailConfigBO;
       }
        return null;
    }

    @Override
    public EmailConfigVO detail() {
        List<SysInfoEntity> list = baseRepository.list();
        if (list.size() > 0) {
            EmailConfigVO vo = new EmailConfigVO();
            SysInfoEntity entity = list.get(0);
            vo.setEmailHost(entity.getEmailHost());
            vo.setEmailPort(entity.getEmailPort());
            vo.setEmailUsername(entity.getEmailUsername());
            vo.setEmailPassword(entity.getEmailPassword());
            vo.setEmailFrom(entity.getEmailFrom());
            return vo;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(EmailConfigDTO dto) {
        List<SysInfoEntity> list = baseRepository.list();
        if (list.size() > 0) {
            SysInfoEntity entity = list.get(0);
            entity.setEmailHost(dto.getEmailHost());
            entity.setEmailPort(dto.getEmailPort());
            entity.setEmailUsername(dto.getEmailUsername());
            entity.setEmailPassword(dto.getEmailPassword());
            entity.setEmailFrom(dto.getEmailFrom());
            baseRepository.updateById(entity);
        }
    }
}
