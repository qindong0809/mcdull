package com.dqcer.mcdull.uac.provider.web.manager.uac;

import com.dqcer.mcdull.uac.api.entity.UserEntity;
import com.dqcer.mcdull.uac.api.vo.UserVO;

public interface IUserManager {

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    UserVO entity2VO(UserEntity entity);
}
