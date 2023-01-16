package io.gitee.dqcer.admin.model.convert.sys;


import io.gitee.dqcer.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.admin.model.vo.sys.RoleVO;

/**
 * 角色 对象转换工具类
 *
 * @author dqcer
 * @version 2022/12/26
 */
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
        if (entity == null) {
            return null;
        }
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
