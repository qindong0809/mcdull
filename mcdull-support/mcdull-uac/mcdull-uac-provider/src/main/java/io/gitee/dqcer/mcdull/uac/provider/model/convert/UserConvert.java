package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import cn.hutool.core.convert.Convert;
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
        Long userId = entity.getId();
        userVO.setEmployeeId(Convert.toInt(userId));
        userVO.setLoginName(entity.getLoginName());
        userVO.setLoginPwd(entity.getLoginPwd());
        userVO.setActualName(entity.getActualName());
        userVO.setGender(entity.getGender());
        userVO.setPhone(entity.getPhone());
        Long departmentId = entity.getDepartmentId();
        userVO.setDepartmentId(Convert.toInt(departmentId));
        userVO.setAdministratorFlag(entity.getAdministratorFlag());
        userVO.setRemark(entity.getRemark());
        userVO.setCreatedTime(LocalDateTimeUtil.of(entity.getCreatedTime()));
        userVO.setUpdatedTime(LocalDateTimeUtil.of(entity.getUpdatedTime()));
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
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginName(dto.getLoginName());
        userEntity.setActualName(dto.getActualName());
        userEntity.setGender(dto.getGender());
        userEntity.setPhone(dto.getPhone());
        userEntity.setDepartmentId(dto.getDepartmentId());
        userEntity.setInactive(dto.getDisabledFlag());
        userEntity.setId(dto.getEmployeeId());
        return userEntity;
    }

    public static UserAllVO entityToAllVO(UserEntity userEntity) {
        UserAllVO userAllVO = new UserAllVO();
        userAllVO.setEmployeeId(Convert.toInt(userEntity.getId()));
        userAllVO.setLoginName(userEntity.getLoginName());
        userAllVO.setGender(userEntity.getGender());
        userAllVO.setActualName(userEntity.getActualName());
        userAllVO.setPhone(userEntity.getPhone());
        userAllVO.setDepartmentId(Convert.toInt(userEntity.getDepartmentId()));
        userAllVO.setDisabledFlag(userEntity.getInactive());
        userAllVO.setAdministratorFlag(userEntity.getAdministratorFlag());
        userAllVO.setCreateTime(LocalDateTimeUtil.of(userEntity.getCreatedTime()));
        return userAllVO;
    }
}
