package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigVO;

/**
* 系统配置 对象转换工具类
*
* @author dqcer
* @since 2024-04-29
*/
public class ConfigConvert {


    public static ConfigEntity convertToConfigEntity(ConfigAddDTO item){
        if (item == null){
            return null;
        }
        ConfigEntity entity = new ConfigEntity();
        entity.setConfigName(item.getConfigName());
        entity.setConfigKey(item.getConfigKey());
        entity.setConfigValue(item.getConfigValue());
        entity.setRemark(item.getRemark());
        return entity;
    }



    public static ConfigVO convertToConfigVO(ConfigEntity item){
        if (item == null){
            return null;
        }
        ConfigVO vo = new ConfigVO();
        vo.setId(item.getId());
        vo.setConfigName(item.getConfigName());
        vo.setConfigKey(item.getConfigKey());
        vo.setConfigValue(item.getConfigValue());
        vo.setRemark(item.getRemark());
        vo.setUpdateTime(item.getUpdatedTime());
        vo.setCreateTime(item.getCreatedTime());

        return vo;
    }
    private ConfigConvert() {
    }
}