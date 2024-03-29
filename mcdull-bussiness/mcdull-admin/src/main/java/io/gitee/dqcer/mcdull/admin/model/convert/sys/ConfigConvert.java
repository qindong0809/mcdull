package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ConfigVO;

/**
 * 系统配置转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class ConfigConvert {

    public static ConfigVO convertToConfigVO(SysConfigEntity configDO) {
        ConfigVO configVO = new ConfigVO();
        configVO.setId(configDO.getId());
        configVO.setName(configDO.getName());
        configVO.setConfigKey(configDO.getConfigKey());
        configVO.setConfigValue(configDO.getConfigValue());
        configVO.setConfigType(configDO.getConfigType());
        configVO.setRemark(configDO.getRemark());
        configVO.setCreatedTime(configDO.getCreatedTime());
        return configVO;
    }

    public static SysConfigEntity convertTOConfigDo(ConfigAddDTO dto) {
        SysConfigEntity sysConfigDO = new SysConfigEntity();
        sysConfigDO.setName(dto.getName());
        sysConfigDO.setConfigKey(dto.getConfigKey());
        sysConfigDO.setConfigValue(dto.getConfigValue());
        sysConfigDO.setConfigType(dto.getConfigType());
        sysConfigDO.setRemark(dto.getRemark());
        return sysConfigDO;
    }
}
