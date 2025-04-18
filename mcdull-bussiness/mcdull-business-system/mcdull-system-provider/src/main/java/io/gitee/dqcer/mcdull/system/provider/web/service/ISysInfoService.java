package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.bo.EmailConfigBO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EmailConfigDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.EmailConfigVO;

/**
 * Sys info service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISysInfoService {

    EmailConfigBO getEmailConfig();

    EmailConfigVO detail();

    void update(EmailConfigDTO dto);
}
