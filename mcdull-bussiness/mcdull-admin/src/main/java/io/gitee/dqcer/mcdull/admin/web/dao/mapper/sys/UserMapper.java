package io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 mapper
 *
 * @author dqcer
 * @version 2022/12/26
 */
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
