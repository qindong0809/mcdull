package com.dqcer.mcdull.uac.provider.web.manager.uac;

import com.dqcer.mcdull.uac.provider.model.entity.UserDO;
import com.dqcer.mcdull.uac.provider.model.vo.UserVO;

/**
 * 用户 通过逻辑定义
 *
 * @author dqcer
 * @date 2022/12/26
 */
public interface IUserManager {

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    UserVO entity2VO(UserDO entity);
}
