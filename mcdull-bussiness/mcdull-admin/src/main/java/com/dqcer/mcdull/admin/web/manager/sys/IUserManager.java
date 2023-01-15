package com.dqcer.mcdull.admin.web.manager.sys;


import com.dqcer.mcdull.admin.model.entity.sys.UserDO;
import com.dqcer.mcdull.admin.model.vo.sys.UserVO;

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
    UserVO entityToVo(UserDO entity);
}
