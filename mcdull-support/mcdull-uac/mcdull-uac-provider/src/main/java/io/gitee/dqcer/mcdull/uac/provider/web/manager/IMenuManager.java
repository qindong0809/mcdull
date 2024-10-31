package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;

import java.util.List;
import java.util.Map;

/**
 * Role 管理器
 *
 * @author dqcer
 * @since 2024/10/31
 */
public interface IMenuManager {

    /**
     * 获取菜单名称
     *
     * @param codeList 代码列表
     * @return {@link Map }<{@link String }, {@link MenuEntity }>
     */
    Map<String, MenuEntity> getMenuName(List<String> codeList);

    /**
     * 列出全部
     *
     * @return {@link List }<{@link MenuEntity }>
     */
    List<MenuEntity> listAll();
}
