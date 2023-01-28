package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;

/**
 * 菜单 数据库操作封装接口层
 *
 * @author dqcer
 * @version 2022/12/26
 */
public interface IMenuRepository extends IService<MenuDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    Page<MenuDO> selectPage(MenuLiteDTO dto);
}
