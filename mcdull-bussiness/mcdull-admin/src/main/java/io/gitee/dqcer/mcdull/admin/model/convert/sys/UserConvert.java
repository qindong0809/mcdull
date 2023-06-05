package io.gitee.dqcer.mcdull.admin.model.convert.sys;


import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;

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
    public static UserVO entityToVO(UserDO entity) {
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
        userVO.setNickName(entity.getNickName());
        userVO.setAccount(entity.getAccount());
        userVO.setEmail(entity.getEmail());
        userVO.setPhone(entity.getPhone());
        userVO.setLastLoginTime(entity.getLastLoginTime());
        userVO.setType(entity.getType());
        userVO.setDeptId(entity.getDeptId());
        return userVO;
    }

    public static UserDO dtoToEntity(UserInsertDTO dto) {
        UserDO userDO = new UserDO();
        userDO.setNickName(dto.getNickName());
        userDO.setAccount(dto.getAccount());
        userDO.setPassword(dto.getPassword());
        userDO.setEmail(dto.getEmail());
        userDO.setPhone(dto.getPhone());
        userDO.setDeptId(dto.getDeptId());
        userDO.setStatus(dto.getStatus());
        return userDO;
    }

    public static UserDetailVO convertToUserDetailVO(UserDO userDO) {
        UserDetailVO userDetailVO = new UserDetailVO();
        userDetailVO.setType(userDO.getType());
        userDetailVO.setNickName(userDO.getNickName());
        userDetailVO.setId(userDO.getId());
        userDetailVO.setStatus(userDO.getStatus());
        userDetailVO.setAccount(userDO.getAccount());
        userDetailVO.setEmail(userDO.getEmail());
        userDetailVO.setPhone(userDO.getPhone());
        userDetailVO.setDeptId(userDO.getDeptId());
        return userDetailVO;

    }
}
