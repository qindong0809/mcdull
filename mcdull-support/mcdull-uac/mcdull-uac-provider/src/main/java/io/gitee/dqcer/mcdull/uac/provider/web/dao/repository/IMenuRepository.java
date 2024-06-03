package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
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

    List<String> allCodeList();

    List<MenuEntity> allList();

    Map<Integer, List<String>> menuCodeListMap(Map<Integer, List<Integer>> menuListMap);

    List<MenuEntity> list(Collection<Integer> collection);

    List<MenuEntity> all();

    List<MenuEntity> allAndButton();

    List<MenuEntity> listByParentId(Integer parentId);

    boolean delete(Integer id, String reason);

    Map<Integer, List<MenuEntity>> getMenuListMap(Map<Integer, List<Integer>> menuListMap);

    List<MenuEntity> listOnlyMenu(Boolean onlyMenu);
}
