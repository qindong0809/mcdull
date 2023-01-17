package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;

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
     * @return {@link Page}<{@link MenuDO}>
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
