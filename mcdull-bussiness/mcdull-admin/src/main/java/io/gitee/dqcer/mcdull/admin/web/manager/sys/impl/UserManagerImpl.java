package io.gitee.dqcer.mcdull.admin.web.manager.sys.impl;

import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRoleRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户通用逻辑实现层
 *
 * @author dqcer
 * @version 2022/12/25
 */
@Service
public class UserManagerImpl implements IUserManager {

    @Resource
    private IUserRepository userRepository;

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
        UserVO vo = UserConvert.entity2VO(entity);
        if (vo == null) {
            return null;
        }
        List<BaseVO> baseRoles = new ArrayList<>();
        List<Long> list = userRoleRepository.listRoleByUserId(vo.getId());
        if (!list.isEmpty()) {
            for (RoleDO roleDO : roleRepository.listByIds(list)) {
                BaseVO role = new BaseVO();
                role.setId(roleDO.getId());
                role.setName(roleDO.getName());
                baseRoles.add(role);
            }
        }
        vo.setRoles(baseRoles);
        return vo;
    }
}
