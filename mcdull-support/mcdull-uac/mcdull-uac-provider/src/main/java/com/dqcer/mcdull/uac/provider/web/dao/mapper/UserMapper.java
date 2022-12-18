package com.dqcer.mcdull.uac.provider.web.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import com.dqcer.mcdull.uac.provider.model.entity.UserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserPowerVO}>
     */
    List<UserPowerVO> queryRoles(@Param("userId") Long userId);

    /**
     * 通过角色id查询模块
     *
     * @param roleId 角色id
     * @return {@link List}<{@link String}>
     */
    List<String> queryModulesByRoleId(@Param("roleId") Long roleId);
}
