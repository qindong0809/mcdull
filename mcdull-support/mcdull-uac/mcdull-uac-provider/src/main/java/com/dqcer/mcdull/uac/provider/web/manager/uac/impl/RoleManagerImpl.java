package com.dqcer.mcdull.uac.provider.web.manager.uac.impl;

import com.dqcer.mcdull.uac.api.convert.RoleConvert;
import com.dqcer.mcdull.uac.api.entity.RoleDO;
import com.dqcer.mcdull.uac.api.vo.RoleVO;
import com.dqcer.mcdull.uac.api.vo.UserVO;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import com.dqcer.mcdull.uac.provider.web.manager.uac.IRoleManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public RoleVO entity2VO(RoleDO entity) {
        RoleVO vo = RoleConvert.entity2VO(entity);
        if (vo == null) {
            return null;
        }
        vo.setCreatedByStr(userRepository.getById(entity.getCreatedBy()).getNickname());
        Long updatedBy = entity.getUpdatedBy();
        if (updatedBy != null) {
            vo.setUpdatedByStr(userRepository.getById(updatedBy).getNickname());
        }
        return vo;
    }
}
