package io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.impl;

import io.gitee.dqcer.mcdull.uac.provider.model.convert.UserConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.IUserManager;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public UserVO entity2VO(UserDO entity) {
        UserVO vo = UserConvert.entity2VO(entity);
        if (vo == null) {
            return null;
        }
        vo.setCreatedByStr(userRepository.getById(entity.getCreatedBy()).getNickName());
        Long updatedBy = entity.getUpdatedBy();
        if (updatedBy != null) {
            vo.setUpdatedByStr(userRepository.getById(updatedBy).getNickName());
        }

        List<BaseVO> baseRoles = new ArrayList<>();
        List<Long> list = userRoleRepository.listRoleByUserId(vo.getId());
        if (!list.isEmpty()) {
            for (RoleDO roleDO : roleRepository.listByIds(list)) {
//                BaseVO role = new BaseVO();
//                role.setId(roleDO.getId());
//                role.setName(roleDO.getName());
//                baseRoles.add(role);
            }
        }
        vo.setRoles(baseRoles);
        return vo;
    }
}
