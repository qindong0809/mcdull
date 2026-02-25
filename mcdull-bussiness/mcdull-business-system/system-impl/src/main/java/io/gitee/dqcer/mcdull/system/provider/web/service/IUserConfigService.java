package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserConfigEntity;

public interface IUserConfigService extends IRepository<UserConfigEntity> {

    /**
     * get
     *
     * @param userId userId
     * @return {@link UserConfigEntity }
     */
    UserConfigEntity get(Integer userId);
}
