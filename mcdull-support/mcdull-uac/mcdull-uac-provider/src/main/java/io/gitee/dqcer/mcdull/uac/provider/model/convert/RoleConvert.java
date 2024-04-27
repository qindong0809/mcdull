package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;

/**
 * 角色 对象转换工具类
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class RoleConvert {

    public static RoleVO entity2VO(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        roleVO.setId(entity.getId());
        roleVO.setRoleName(entity.getRoleName());
        roleVO.setRoleCode(entity.getRoleCode());
        return roleVO;
    }

    public static RoleEntity insertToEntity(RoleInsertDTO dto) {
        RoleEntity roleDO = new RoleEntity();
        roleDO.setRoleName(dto.getRoleName());
        roleDO.setRoleCode(dto.getRoleCode());
        roleDO.setRoleCode(roleDO.getRoleCode());
        return roleDO;
    }

    public static RoleVO entityToVO(RoleEntity entity) {
        RoleVO roleVO = new RoleVO();
        roleVO.setId(entity.getId());
        roleVO.setRoleName(entity.getRoleName());
        roleVO.setRoleCode(entity.getRoleCode());
        roleVO.setRemark(entity.getRemark());
        return roleVO;
    }
}
