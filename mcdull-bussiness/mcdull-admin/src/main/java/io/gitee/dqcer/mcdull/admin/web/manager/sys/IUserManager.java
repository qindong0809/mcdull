package io.gitee.dqcer.mcdull.admin.web.manager.sys;


import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleEntity;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;

import java.util.List;

/**
 * 用户 通用逻辑定义
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserManager {

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    UserVO entityToVo(UserEntity entity);


    /**
     * 得到用户角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link RoleEntity}>
     */
    List<RoleEntity> getUserRoles(Long userId);

    UserDetailVO entityToDetailVo(UserEntity userDO);
}
