package io.gitee.dqcer.mcdull.admin.model.convert.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.ConfigEnvLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.ConfigEnvEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.database.ConfigEnvVO;

/**
*  对象转换工具类
*
* @author dqcer
* @since 2023-08-29
*/
public class ConfigEnvConvert {

   /**
    * ConfigEnvLiteDTO转换为ConfigEnvDO
    *
    * @param item ConfigEnvLiteDTO
    * @return {@link ConfigEnvEntity}
    */
    public static ConfigEnvEntity convertToConfigEnvDO(ConfigEnvLiteDTO item){
        if (item == null){
            return null;
        }
        ConfigEnvEntity entity = new ConfigEnvEntity();
        entity.setId(item.getId());
        entity.setType(item.getType());
        entity.setDynamicTitle(item.getDynamicTitle() ? 1 : 2);
        entity.setFixedHeader(item.getFixedHeader() ? 1 : 2);
        entity.setSideTheme(item.getSideTheme());
        entity.setSidebarLogo(item.getSidebarLogo() ? 1 : 2);
        entity.setTagsView(item.getTagsView() ? 1 : 2);
        entity.setTheme(item.getTheme());
        entity.setTopNav(item.getTopNav() ? 1 : 2);

        return entity;
    }


    /**
    * ConfigEnvDOConfigEnvVO
    *
    * @param item ConfigEnvDO
    * @return {@link ConfigEnvVO}
    */
    public static ConfigEnvVO convertToConfigEnvVO(ConfigEnvEntity item){
        if (item == null){
            return null;
        }
        ConfigEnvVO vo = new ConfigEnvVO();
        vo.setId(item.getId());
        vo.setType(item.getType());
        vo.setDynamicTitle(item.getDynamicTitle() == 1);
        vo.setFixedHeader(item.getFixedHeader() == 1);
        vo.setSideTheme(item.getSideTheme());
        vo.setSidebarLogo(item.getSidebarLogo() == 1);
        vo.setTagsView(item.getTagsView() == 1);
        vo.setTheme(item.getTheme());
        vo.setTopNav(item.getTopNav() == 1);

        return vo;
    }
    private ConfigEnvConvert() {
    }
}