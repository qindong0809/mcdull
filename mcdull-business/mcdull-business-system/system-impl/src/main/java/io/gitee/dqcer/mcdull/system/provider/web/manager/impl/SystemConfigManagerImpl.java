package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import io.gitee.dqcer.mcdull.system.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ISystemConfigManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.ConfigMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置只读实现，缓存策略与原 CommonManager 保持一致。
 */
@Service
public class SystemConfigManagerImpl implements ISystemConfigManager {

    @Resource
    private ICache redisCache;
    @Resource
    private ConfigMapper configMapper;

    @Override
    public String getConfig(String key) {
        List<ConfigEntity> entityList = redisCache.getListOrSetPrefixed("sys_config", ConfigEntity.class,
                () -> configMapper.selectList(null), 60 * 60 * 24);
        if (CollUtil.isNotEmpty(entityList)) {
            Map<String, String> map = entityList.stream().collect(Collectors.toMap(ConfigEntity::getConfigKey, ConfigEntity::getConfigValue));
            return map.get(key);
        }
        return null;
    }

    @Override
    public Boolean isCaptchaEnabled() {
        String config = this.getConfig("login-captcha");
        if (StrUtil.isNotBlank(config)) {
            return BooleanUtil.toBooleanObject(config);
        }
        return false;
    }

    @Override
    public Boolean getConfigToBool(String key) {
        String config = this.getConfig(key);
        if (StrUtil.isNotBlank(config)) {
            return BooleanUtil.toBooleanObject(config);
        }
        return null;
    }
}
