package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuEntity;

import java.util.List;

/**
 * 菜单 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IMenuRepository extends IService<MenuEntity> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link MenuEntity}>
     */
    Page<MenuEntity> selectPage(MenuLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(MenuEntity entity);

    /**
     * 获取菜单
     *
     * @param menuIds 菜单id
     * @return {@link List}<{@link MenuEntity}>
     */
    List<MenuEntity> getMenuByIds(List<Long> menuIds);

    List<MenuEntity> list(String menuName, String status, List<Long> menuIds);

    List<MenuEntity> getListByName(String name);

    List<MenuEntity> getSubMenuListByParentId(Long parentId);

    List<MenuEntity> getAllMenu();
}
