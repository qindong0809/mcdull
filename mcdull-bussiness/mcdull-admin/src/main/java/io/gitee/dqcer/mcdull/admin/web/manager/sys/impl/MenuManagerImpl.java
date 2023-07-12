package io.gitee.dqcer.mcdull.admin.web.manager.sys.impl;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.enums.MenuTypeEnum;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IMenuManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单通用逻辑实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class MenuManagerImpl implements IMenuManager {

    @Resource
    private IMenuRepository menuRepository;


    /**
     * 是否为菜单内部跳转
     *
     * @param menuDO 菜单
     * @return boolean
     */
    @Override
    public boolean isMenuFrame(MenuDO menuDO) {
        return menuDO.getParentId().intValue() == 0 && MenuTypeEnum.MENU.getCode().equals(menuDO.getMenuType())
                && menuDO.getIsFrame().equals("1");
    }

    /**
     * 是否为内链组件
     *
     * @param menuDO 菜单
     * @return boolean
     */
    @Override
    public boolean isInnerLink(MenuDO menuDO) {
        return menuDO.getIsFrame().equals("1") && menuDO.getPath().contains("http");
    }

    /**
     * 是否为parent_view组件
     *
     * @param menuDO 菜单
     * @return boolean
     */
    @Override
    public boolean isParentView(MenuDO menuDO) {
        return menuDO.getParentId().intValue() != 0 && "M".equals(menuDO.getMenuType());
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    @Override
    public String getRouterPath(MenuDO menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        if (0 == menu.getParentId().intValue() && MenuTypeEnum.DIRECTORY.getCode().equals(menu.getMenuType()) && "1".equals(menu.getIsFrame())) {
            // 非外链并且是一级目录（类型为目录）
            routerPath = "/" + menu.getPath();
        } else if (isMenuFrame(menu)) {
            // 非外链并且是一级目录（类型为菜单）
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    @Override
    public String getComponent(MenuDO menu) {
        String component = "Layout";
        if (StrUtil.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StrUtil.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = "InnerLink";
        } else if (StrUtil.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = "ParentView";
        }
        return component;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @param path 路径
     * @return {@link String} 替换后的内链域名
     */
    @Override
    public String innerLinkReplaceEach(String path) {
        return path;
    }

    @Override
    public List<MenuDO> getAllMenu() {
        return menuRepository.getAllMenu();
    }
}
