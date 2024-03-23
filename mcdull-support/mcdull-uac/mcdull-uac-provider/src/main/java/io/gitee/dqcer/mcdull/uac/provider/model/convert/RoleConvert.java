package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;

/**
 * 角色 对象转换工具类
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class RoleConvert {

    public static RoleDO dto2Entity(RoleLiteDTO dto) {
        RoleDO entity = new RoleDO();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static RoleVO entity2VO(RoleDO entity) {
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

    public static RoleDO insertToEntity(RoleInsertDTO dto) {
        RoleDO roleDO = new RoleDO();
        roleDO.setName(dto.getName());
        roleDO.setDescription(dto.getDescription());
        return roleDO;
    }

    public static RoleVO entityToVO(RoleDO entity) {
        RoleVO roleVO = new RoleVO();
        roleVO.setType(entity.getType());
        roleVO.setId(entity.getId());
        roleVO.setCreatedTime(entity.getCreatedTime());
        roleVO.setCreatedBy(entity.getCreatedBy());
        roleVO.setUpdatedTime(entity.getUpdatedTime());
        roleVO.setUpdatedBy(entity.getUpdatedBy());
        roleVO.setName(entity.getName());
        roleVO.setDescription(entity.getDescription());
        roleVO.setInactive(entity.getInactive());
        return roleVO;
    }
}
