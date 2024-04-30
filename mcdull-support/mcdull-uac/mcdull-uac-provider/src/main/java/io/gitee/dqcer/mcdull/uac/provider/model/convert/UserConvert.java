package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
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
        entity.setLoginPwd(dto.getLoginPwd());
        entity.setGender(dto.getGender());
        entity.setPhone(dto.getPhone());
        entity.setDepartmentId(dto.getDepartmentId());
        entity.setAdministratorFlag(dto.getAdministratorFlag());
        entity.setRemark(dto.getRemark());
        return entity;

    }
}
