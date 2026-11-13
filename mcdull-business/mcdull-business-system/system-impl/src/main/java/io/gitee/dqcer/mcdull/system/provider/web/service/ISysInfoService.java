package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.bo.EmailConfigBO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EmailConfigDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SysInfoEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.EmailConfigVO;

/**
 * Sys info service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISysInfoService extends IRepository<SysInfoEntity> {

    /**
     * get config
     *
     * @return {@link EmailConfigBO }
     */
    EmailConfigBO getEmailConfig();

    /**
     * detail
     *
     * @return {@link EmailConfigVO }
     */
    EmailConfigVO detail();

    /**
     * update
     *
     * @param dto DTO
     */
    void update(EmailConfigDTO dto);
}
