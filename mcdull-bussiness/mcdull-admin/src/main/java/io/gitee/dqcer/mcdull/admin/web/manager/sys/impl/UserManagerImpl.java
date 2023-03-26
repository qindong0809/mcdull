package io.gitee.dqcer.mcdull.admin.web.manager.sys.impl;

import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户通用逻辑实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserManagerImpl implements IUserManager {

    @Resource
    private IUserRoleRepository userRoleRepository;

    @Resource
    private IRoleRepository roleRepository;

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    @Override
    public UserVO entityToVo(UserDO entity) {
        UserVO vo = UserConvert.entityToVO(entity);
        if (vo == null) {
            return null;
        }
        List<BaseVO> baseRoles = this.getBaseRoles(vo.getId());
        vo.setRoles(baseRoles);
        return vo;
    }

    @Override
    public UserDetailVO entityToDetailVo(UserDO userDO) {
        UserDetailVO vo = UserConvert.convertToUserDetailVO(userDO);
        List<BaseVO> baseRoles = this.getBaseRoles(vo.getId());
        vo.setRoles(baseRoles);
        return vo;
    }

    private List<BaseVO> getBaseRoles(Long vo) {
        List<BaseVO> baseRoles = new ArrayList<>();
        List<Long> list = userRoleRepository.listRoleByUserId(vo);
        if (!list.isEmpty()) {
            for (RoleDO roleDO : roleRepository.listByIds(list)) {
                BaseVO role = new BaseVO();
                role.setId(roleDO.getId());
                role.setName(roleDO.getName());
                baseRoles.add(role);
            }
        }
        return baseRoles;
    }

    /**
     * 得到用户角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link RoleDO}>
     */
    @Override
    public List<RoleDO> getUserRoles(Long userId) {
        List<Long> roleIds = userRoleRepository.listRoleByUserId(userId);
        if (!roleIds.isEmpty()) {
            List<RoleDO> roleDOList = roleRepository.listByIds(roleIds);
            if (!roleDOList.isEmpty()) {
                return roleDOList;
            }
        }
        return Collections.emptyList();
    }
}
