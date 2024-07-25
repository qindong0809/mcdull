package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.PasswordPolicyDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PasswordPolicyVO;

/**
 * Password Policy Service
 *
 * @author dqcer
 * @since 2024/7/25 9:27
 */

public interface IPasswordPolicyService {


    PasswordPolicyVO detail();

    void update(PasswordPolicyDTO dto);
}
