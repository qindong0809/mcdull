package com.dqcer.mcdull.uac.provider.web.manager.uac.impl;

import com.dqcer.mcdull.uac.api.convert.UserConvert;
import com.dqcer.mcdull.uac.api.entity.UserDO;
import com.dqcer.mcdull.uac.api.vo.UserVO;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import com.dqcer.mcdull.uac.provider.web.manager.uac.IUserManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserManagerImpl implements IUserManager {

    @Resource
    private IUserRepository userRepository;

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
        vo.setCreatedByStr(userRepository.getById(entity.getCreatedBy()).getNickname());
        Long updatedBy = entity.getUpdatedBy();
        if (updatedBy != null) {
            vo.setUpdatedByStr(userRepository.getById(updatedBy).getNickname());
        }
        return vo;
    }
}
