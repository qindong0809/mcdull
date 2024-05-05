package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserAllVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;

/**
 * 用户 对象转换工具类
 *
 * @author dqcer
 * @since  2022/11/25
 */
public class UserConvert {


    /**
     * 实体来转视图对象
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    public static UserVO entityToVO(UserEntity entity) {
        UserVO userVO = new UserVO();
        userVO.setEmployeeId(entity.getId());
        userVO.setLoginName(entity.getLoginName());
        userVO.setLoginPwd(entity.getLoginPwd());
        userVO.setActualName(entity.getActualName());
        userVO.setGender(entity.getGender());
        userVO.setPhone(entity.getPhone());
        userVO.setDepartmentId(entity.getDepartmentId());
        userVO.setAdministratorFlag(entity.getAdministratorFlag());
        userVO.setRemark(entity.getRemark());
        return userVO;
    }

    public static UserEntity insertDtoToEntity(UserAddDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setActualName(dto.getActualName());
        entity.setLoginName(dto.getLoginName());
        entity.setGender(dto.getGender());
        entity.setPhone(dto.getPhone());
        entity.setDepartmentId(dto.getDepartmentId());
        return entity;

    }

    public static UserEntity updateDtoToEntity(UserUpdateDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setLoginName(dto.getLoginName());
        entity.setGender(dto.getGender());
        entity.setPhone(dto.getPhone());
        entity.setDepartmentId(dto.getDepartmentId());
        return entity;

    }

    public static UserAllVO entityToAllVO(UserEntity userEntity) {
        UserAllVO userAllVO = new UserAllVO();
        userAllVO.setEmployeeId(userEntity.getId());
        userAllVO.setLoginName(userEntity.getLoginName());
        userAllVO.setGender(userEntity.getGender());
        userAllVO.setActualName(userEntity.getActualName());
        userAllVO.setPhone(userEntity.getPhone());
        userAllVO.setDepartmentId(userEntity.getDepartmentId());
        userAllVO.setDisabledFlag(userEntity.getInactive());
        userAllVO.setAdministratorFlag(userEntity.getAdministratorFlag());
        userAllVO.setCreateTime(LocalDateTimeUtil.of(userEntity.getCreatedTime()));
        return userAllVO;
    }
}
