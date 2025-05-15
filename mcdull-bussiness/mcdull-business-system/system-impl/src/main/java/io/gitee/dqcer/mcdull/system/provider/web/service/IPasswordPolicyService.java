package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.dto.PasswordPolicyDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.PasswordPolicyVO;

/**
 * Password Policy Service
 *
 * @author dqcer
 * @since 2024/7/25 9:27
 */

public interface IPasswordPolicyService {

    /**
     * 详情
     *
     * @return {@link PasswordPolicyVO }
     */
    PasswordPolicyVO detail();

    /**
     * 更新
     *
     * @param dto DTO
     */
    void update(PasswordPolicyDTO dto);
}
