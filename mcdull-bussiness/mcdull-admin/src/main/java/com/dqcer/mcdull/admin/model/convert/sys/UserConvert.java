package com.dqcer.mcdull.admin.model.convert.sys;


import com.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import com.dqcer.mcdull.admin.model.entity.sys.UserDO;
import com.dqcer.mcdull.admin.model.vo.sys.UserVO;

/**
 * 用户 对象转换工具类
 *
 * @author dqcer
 * @version  2022/11/25
 */
public class UserConvert {


    /**
     * 实体来转视图对象
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    public static UserVO entity2VO(UserDO entity) {
        if (entity == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        userVO.setId(entity.getId());
        userVO.setCreatedTime(entity.getCreatedTime());
        userVO.setCreatedBy(entity.getCreatedBy());
        userVO.setUpdatedTime(entity.getUpdatedTime());
        userVO.setUpdatedBy(entity.getUpdatedBy());
        userVO.setStatus(entity.getStatus());
        userVO.setDelFlag(entity.getDelFlag());
        userVO.setNickname(entity.getNickname());
        userVO.setAccount(entity.getAccount());
        userVO.setEmail(entity.getEmail());
        userVO.setPhone(entity.getPhone());
        userVO.setLastLoginTime(entity.getLastLoginTime());
        userVO.setType(entity.getType());
        return userVO;
    }

    public static UserDO dto2Entity(UserLiteDTO dto) {
        UserDO entity = new UserDO();
        entity.setNickname(dto.getNickname());
        entity.setAccount(dto.getAccount());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        return entity;
    }
}