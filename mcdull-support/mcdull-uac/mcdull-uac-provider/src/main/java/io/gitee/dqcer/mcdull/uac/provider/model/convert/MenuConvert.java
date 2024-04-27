package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuVO;

/**
 *  对象转换工具类
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class MenuConvert {


    public static MenuVO toVO(MenuEntity entity) {
        if (entity != null) {
            MenuVO vo = new MenuVO();
            vo.setMenuId(Convert.toInt(entity.getId()));
            vo.setPath(entity.getPath());
            vo.setApiPerms(entity.getApiPerms());
            vo.setContextMenuId(Convert.toInt(entity.getContextMenuId()));
            vo.setFrameUrl(entity.getFrameUrl());
            vo.setVisibleFlag(entity.getVisibleFlag());
            vo.setSort(entity.getSort());
            vo.setMenuName(entity.getMenuName());
            vo.setPermsType(entity.getPermsType());
            vo.setIcon(entity.getIcon());
            vo.setCacheFlag(entity.getCacheFlag());
            vo.setMenuType(entity.getMenuType());
            vo.setParentId(Convert.toInt(entity.getParentId()));
            vo.setComponent(entity.getComponent());
            vo.setWebPerms(entity.getWebPerms());
            vo.setFrameFlag(entity.getFrameFlag());
            return vo;
        }
        return null;
    }
}
