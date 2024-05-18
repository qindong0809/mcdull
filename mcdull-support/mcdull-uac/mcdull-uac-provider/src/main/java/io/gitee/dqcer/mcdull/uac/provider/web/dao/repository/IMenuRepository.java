package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 菜单 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IMenuRepository extends IService<MenuEntity> {

    Page<MenuEntity> selectPage(MenuLiteDTO dto);

    List<String> allCodeList();

    List<MenuEntity> allList();

    Map<Long, List<String>> menuCodeListMap(Map<Long, List<Long>> menuListMap);

    List<MenuEntity> list(Collection<Long> collection);

    List<MenuEntity> all();

    List<MenuEntity> allAndButton();

    List<MenuEntity> listByParentId(Long parentId);

    boolean delete(Long id, String reason);

    Map<Long, List<MenuEntity>> getMenuListMap(Map<Long, List<Long>> menuListMap);

    List<MenuEntity> listOnlyMenu(Boolean onlyMenu);
}
