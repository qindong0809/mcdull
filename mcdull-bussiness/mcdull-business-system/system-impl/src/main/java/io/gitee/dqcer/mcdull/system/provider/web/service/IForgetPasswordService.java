package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.dto.ForgetPasswordRequestDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ForgetPasswordRestDTO;

/**
 * forget password
 *
 * @author dqcer
 * @since 2024/06/11
 */
public interface IForgetPasswordService {

    String request(ForgetPasswordRequestDTO dto);

    void reset(Integer userId, ForgetPasswordRestDTO dto);
}
