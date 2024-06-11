package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ForgetPasswordRequestDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IForgetPasswordService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * forget password service impl
 *
 * @author dqcer
 * @since 2024/06/11
 */
@Service
public class ForgetPasswordServiceImpl implements IForgetPasswordService {

    @Resource
    private IUserService userService;

    @Override
    public String request(ForgetPasswordRequestDTO dto) {
        UserEntity user = userService.get(dto.getUserIdentity());
        if (ObjectUtil.isNull(user)) {

        }
        return null;
    }
}
