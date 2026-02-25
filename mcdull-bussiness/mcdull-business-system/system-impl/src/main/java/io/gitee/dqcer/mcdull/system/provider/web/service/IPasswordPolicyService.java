package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.PasswordPolicyDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.PasswordPolicyEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.PasswordPolicyVO;

/**
 * Password Policy Service
 *
 * @author dqcer
 * @since 2024/7/25 9:27
 */

public interface IPasswordPolicyService extends IRepository<PasswordPolicyEntity> {

    /**
     * detail
     *
     * @return {@link PasswordPolicyVO }
     */
    PasswordPolicyVO detail();

    /**
     * update
     *
     * @param dto DTO
     */
    void update(PasswordPolicyDTO dto);
}
