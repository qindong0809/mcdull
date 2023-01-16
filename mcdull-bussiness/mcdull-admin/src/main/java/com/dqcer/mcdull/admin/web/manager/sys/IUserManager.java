package io.gitee.dqcer.admin.web.manager.sys;


import io.gitee.dqcer.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.admin.model.vo.sys.UserVO;

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
