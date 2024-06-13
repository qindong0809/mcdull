package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.bo.EmailConfigBO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EmailConfigDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EmailConfigVO;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface ISysInfoService {

    EmailConfigBO getEmailConfig();

    EmailConfigVO detail();

    void update(EmailConfigDTO dto);
}
