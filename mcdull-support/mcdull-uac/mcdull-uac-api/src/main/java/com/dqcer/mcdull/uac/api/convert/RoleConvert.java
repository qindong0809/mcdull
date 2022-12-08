package com.dqcer.mcdull.uac.api.convert;

import com.dqcer.mcdull.uac.api.dto.RoleLiteDTO;
import com.dqcer.mcdull.uac.api.entity.RoleDO;
import com.dqcer.mcdull.uac.api.vo.RoleVO;

public class RoleConvert {

    public static RoleDO dto2Entity(RoleLiteDTO dto) {
        RoleDO entity = new RoleDO();
        entity.setDelFlag(dto.getDelFlag());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static RoleVO entity2VO(RoleDO entity) {
        RoleVO roleVO = new RoleVO();
        roleVO.setId(entity.getId());
        roleVO.setCreatedTime(entity.getCreatedTime());
        roleVO.setCreatedBy(entity.getCreatedBy());
        roleVO.setStatus(entity.getStatus());
        roleVO.setDelFlag(entity.getDelFlag());
        roleVO.setName(entity.getName());
        roleVO.setDescription(entity.getDescription());
        roleVO.setType(entity.getType());
        return roleVO;
    }
}
