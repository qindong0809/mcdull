package io.gitee.dqcer.mcdull.admin.model.convert.sys;


import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleVO;

/**
 * 角色 对象转换工具类
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class RoleConvert {

    public static RoleEntity convertToRoleDO(RoleInsertDTO dto) {
        RoleEntity roleDO = new RoleEntity();
        roleDO.setName(dto.getName());
        roleDO.setCode(dto.getCode());
        roleDO.setDescription(dto.getDescription());
        roleDO.setStatus(dto.getStatus());
        return roleDO;
    }

    public static RoleVO convertToRoleVO(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        roleVO.setRoleId(entity.getId());
        roleVO.setCode(entity.getCode());
        roleVO.setName(entity.getName());
        roleVO.setCreatedTime(entity.getCreatedTime());
        roleVO.setUpdatedTime(entity.getUpdatedTime());
        roleVO.setStatus(entity.getStatus());
        roleVO.setType(entity.getType());
        return roleVO;
    }
}
