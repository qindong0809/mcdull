package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.PasswordPolicyDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.PasswordPolicyVO;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IPasswordPolicyService {


    PasswordPolicyVO detail();

    void update(PasswordPolicyDTO dto);
}
