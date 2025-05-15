package io.gitee.dqcer.mcdull.system.provider.model.convert;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.system.provider.model.dto.RoleAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;

/**
 * 角色 对象转换工具类
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class RoleConvert {

    public static RoleEntity insertToEntity(RoleAddDTO dto) {
        RoleEntity roleDO = new RoleEntity();
        roleDO.setRoleName(dto.getRoleName());
        roleDO.setRoleCode(dto.getRoleCode());
        roleDO.setRoleCode(roleDO.getRoleCode());
        roleDO.setRemark(dto.getRemark());
        return roleDO;
    }

    public static RoleVO entityToVO(RoleEntity entity) {
        RoleVO roleVO = new RoleVO();
        roleVO.setRoleId(Convert.toInt(entity.getId()));
        roleVO.setRoleName(entity.getRoleName());
        roleVO.setRoleCode(entity.getRoleCode());
        roleVO.setRemark(entity.getRemark());
        return roleVO;
    }
}
