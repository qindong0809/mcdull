package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.uac.api.dto.RoleLiteDTO;
import com.dqcer.mcdull.uac.api.entity.RoleEntity;

public interface IRoleRepository extends IService<RoleEntity> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleEntity}>
     */
    Page<RoleEntity> selectPage(RoleLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(RoleEntity entity);
}
