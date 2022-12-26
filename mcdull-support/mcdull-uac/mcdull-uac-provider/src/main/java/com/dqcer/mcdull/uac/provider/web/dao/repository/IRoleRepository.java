package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import com.dqcer.mcdull.uac.provider.model.entity.RoleDO;

/**
 * 角色 数据库操作封装接口层
 *
 * @author dqcer
 * @version 2022/12/26
 */
public interface IRoleRepository extends IService<RoleDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    Page<RoleDO> selectPage(RoleLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(RoleDO entity);

}
