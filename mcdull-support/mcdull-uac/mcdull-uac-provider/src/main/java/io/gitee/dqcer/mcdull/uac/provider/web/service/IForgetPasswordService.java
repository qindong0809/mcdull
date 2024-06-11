package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRequestDTO;

/**
 * forget password
 *
 * @author dqcer
 * @since 2024/06/11
 */
public interface IForgetPasswordService {

    String request(ForgetPasswordRequestDTO dto);
}
