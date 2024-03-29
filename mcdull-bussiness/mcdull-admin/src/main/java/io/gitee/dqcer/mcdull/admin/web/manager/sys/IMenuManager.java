package io.gitee.dqcer.mcdull.admin.web.manager.sys;


import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuEntity;

import java.util.List;

/**
 * 菜单 通用逻辑定义
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IMenuManager {

    /**
     * 是否为菜单内部跳转
     *
     * @param menuDO 菜单
     * @return boolean
     */
    boolean isMenuFrame(MenuEntity menuDO);


    /**
     * 是否为内链组件
     *
     * @param menuDO 菜单
     * @return boolean
     */
    boolean isInnerLink(MenuEntity menuDO);

    /**
     * 是否为parent_view组件
     *
     * @param menuDO 菜单
     * @return boolean
     */
    boolean isParentView(MenuEntity menuDO);

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    String getRouterPath(MenuEntity menu);

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    String getComponent(MenuEntity menu);

    /**
     * 内链域名特殊字符替换
     *
     * @param path 路径
     * @return {@link String} 替换后的内链域名
     */
    String innerLinkReplaceEach(String path);

    List<MenuEntity> getAllMenu();
}
