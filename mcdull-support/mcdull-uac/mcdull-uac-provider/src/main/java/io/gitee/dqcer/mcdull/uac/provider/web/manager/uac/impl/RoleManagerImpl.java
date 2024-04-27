package io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.impl;

import io.gitee.dqcer.mcdull.uac.provider.model.convert.RoleConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.IRoleManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 角色 通用逻辑实现类
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Service
public class RoleManagerImpl implements IRoleManager {

    @Resource
    private IUserRepository userRepository;

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    @Override
    public RoleVO entity2VO(RoleEntity entity) {
        RoleVO vo = RoleConvert.entity2VO(entity);
        if (vo == null) {
            return null;
        }
//        vo.setCreatedByStr(userRepository.getById(entity.getCreatedBy()).getNickName());
//        Integer updatedBy = entity.getUpdatedBy();
//        if (updatedBy != null) {
//            vo.setUpdatedByStr(userRepository.getById(updatedBy).getNickName());
//        }
        return vo;
    }
}
