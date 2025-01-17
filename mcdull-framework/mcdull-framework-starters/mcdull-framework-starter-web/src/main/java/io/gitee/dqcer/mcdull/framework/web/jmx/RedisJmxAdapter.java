package io.gitee.dqcer.mcdull.framework.web.jmx;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Set;

/**
 * Redis JMX适配器
 *
 * @author dqcer
 * @since 2024/03/27
 */
@Component
@ManagedResource(
        objectName = "Customization:name=io.gitee.dqcer.mcdull.framework.web.jmx.RedisJmxAdapter",
        description = "redis util")
public class RedisJmxAdapter {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private RedisClient redisClient;


    @ManagedOperation(description = "clear all key")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "password", description = "password"
            )})
    public String cleanAllKey(String password) {
        LogHelp.warn(log, "RedisJmxAdapter#cleanAllKey: {}", password);
        String errorMsg = validatePassword(password);
        if (errorMsg != null) {
            return errorMsg;
        }
        Set<String> allKey = redisClient.getAllKey();
        for (String cacheKey : allKey) {
            redisClient.delete(cacheKey);
        }
        return "success";
    }

    @ManagedOperation(description = "remove key by prefix")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "keyPrefix", description = "keyPrefix")
    })
    public String removeByKeyPrefix(String keyPrefix){
        LogHelp.warn(log, "RedisJmxAdapter#removeByKeyPrefix: {}", keyPrefix);
        if(StrUtil.isEmpty(keyPrefix)){
            return "key is empty";
        }
        Set<String> cacheKeys = redisClient.keysByPrefix(keyPrefix);
        for (String cacheKey : cacheKeys) {
            redisClient.delete(cacheKey);
        }
        return "success";
    }

    private String validatePassword(String password) {
        if(StrUtil.isEmpty(password)){
            return "password is empty";
        }
        try {
            int length = password.length();
            int num = 0;
            for (int i = 0; i < length; i++) {
                String c = password.substring(i,i+1);
                num = num + Integer.parseInt(c);
            }
            if (num == 20) {
                return null;
            }
        } catch (Exception ignored) {}
        return "password is not valid";
    }
}
