package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;

import java.util.List;

/**
 * 菜单 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
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

    /**
     * 获取菜单
     *
     * @param menuIds 菜单id
     * @return {@link List}<{@link MenuDO}>
     */
    List<MenuDO> getMenuByIds(List<Long> menuIds);

    List<MenuDO> list(String menuName, String status, List<Long> menuIds);

    List<MenuDO> getListByName(String name);

    List<MenuDO> getSubMenuListByParentId(Long parentId);

    List<MenuDO> getAllMenu();
}
