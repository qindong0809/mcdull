package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ConfigVO;

/**
 * 系统配置转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class ConfigConvert {

    public static ConfigVO convertToConfigVO(SysConfigDO configDO) {
        ConfigVO configVO = new ConfigVO();
        configVO.setId(configDO.getId());
        configVO.setName(configDO.getName());
        configVO.setConfigKey(configDO.getConfigKey());
        configVO.setValue(configDO.getValue());
        configVO.setConfigType(configDO.getConfigType());
        configVO.setRemark(configDO.getRemark());
        configVO.setCreatedTime(configDO.getCreatedTime());
        return configVO;
    }

}
