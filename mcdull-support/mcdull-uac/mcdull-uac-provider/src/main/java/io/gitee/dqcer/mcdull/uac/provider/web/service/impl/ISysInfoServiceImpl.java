package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.crypto.SecureUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.EmailConfigBO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SysInfoEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ISysInfoRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISysInfoService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class ISysInfoServiceImpl extends BasicServiceImpl<ISysInfoRepository> implements ISysInfoService {


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
}
