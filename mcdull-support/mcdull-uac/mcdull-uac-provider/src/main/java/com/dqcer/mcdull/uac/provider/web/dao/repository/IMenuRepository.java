package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.uac.provider.model.dto.MenuLiteDTO;
import com.dqcer.mcdull.uac.provider.model.entity.MenuDO;
import com.dqcer.mcdull.uac.provider.model.entity.RoleDO;

public interface IMenuRepository extends IService<MenuDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    Page<MenuDO> selectPage(MenuLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(MenuDO entity);
}
