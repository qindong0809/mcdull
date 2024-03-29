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
        roleVO.setName(entity.getName());
        roleVO.setDescription(entity.getDescription());
        roleVO.setType(entity.getType());
        return roleVO;
    }

    public static RoleEntity insertToEntity(RoleInsertDTO dto) {
        RoleEntity roleDO = new RoleEntity();
        roleDO.setName(dto.getName());
        roleDO.setDescription(dto.getDescription());
        return roleDO;
    }

    public static RoleVO entityToVO(RoleEntity entity) {
        RoleVO roleVO = new RoleVO();
        roleVO.setType(entity.getType());
        roleVO.setId(entity.getId());
        roleVO.setCreatedTime(entity.getCreatedTime());
        roleVO.setCreatedBy(entity.getCreatedBy());
        roleVO.setUpdatedTime(entity.getUpdatedTime());
        roleVO.setUpdatedBy(entity.getUpdatedBy());
        roleVO.setName(entity.getName());
        roleVO.setCode(entity.getCode());
        roleVO.setDescription(entity.getDescription());
        roleVO.setInactive(entity.getInactive());
        return roleVO;
    }
}
