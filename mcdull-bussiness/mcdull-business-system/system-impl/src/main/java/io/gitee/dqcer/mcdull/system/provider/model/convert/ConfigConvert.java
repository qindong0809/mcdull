package io.gitee.dqcer.mcdull.system.provider.model.convert;

import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ConfigInfoVO;

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

    private ConfigConvert() {
    }

    public static ConfigInfoVO convertToConfigVO(ConfigEntity entity) {
        if (entity != null) {
            ConfigInfoVO vo = new ConfigInfoVO();
            vo.setConfigId(entity.getId());
            vo.setConfigName(entity.getConfigName());
            vo.setConfigKey(entity.getConfigKey());
            vo.setConfigValue(entity.getConfigValue());
            vo.setRemark(entity.getRemark());
            vo.setUpdateTime(entity.getCreatedTime());
            vo.setCreateTime(entity.getCreatedTime());
            return vo;
        }
        return null;
    }
}