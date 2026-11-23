package io.gitee.dqcer.mcdull.framework.redis.jmx;

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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Set;

/**
 * Redis JMX适配器。
 * <p>
 * 危险操作需要通过环境变量或系统属性 {@code MCDULL_JMX_TOKEN} 配置的令牌授权；
 * 未配置时所有危险操作直接拒绝，避免 JMX 端口暴露时被人 Clearing 整个缓存。
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

    private static final String JMX_TOKEN_KEY = "MCDULL_JMX_TOKEN";

    @Resource
    private RedisClient redisClient;

    @ManagedOperation(description = "clear all key (requires MCDULL_JMX_TOKEN)")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "token", description = "value of MCDULL_JMX_TOKEN")
    })
    public String cleanAllKey(String token) {
        String errorMsg = validateToken(token);
        if (errorMsg != null) {
            LogHelp.warn(log, "cleanAllKey rejected: {}", errorMsg);
            return errorMsg;
        }
        Set<String> allKey = redisClient.getAllKey();
        LogHelp.warn(log, "cleanAllKey accepted, keys={}", allKey.size());
        for (String cacheKey : allKey) {
            redisClient.delete(cacheKey);
        }
        return "success, removed=" + allKey.size();
    }

    @ManagedOperation(description = "remove key by prefix (requires MCDULL_JMX_TOKEN)")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "keyPrefix", description = "keyPrefix"),
            @ManagedOperationParameter(name = "token", description = "value of MCDULL_JMX_TOKEN")
    })
    public String removeByKeyPrefix(String keyPrefix, String token) {
        String errorMsg = validateToken(token);
        if (errorMsg != null) {
            LogHelp.warn(log, "removeByKeyPrefix rejected: {}", errorMsg);
            return errorMsg;
        }
        if (StrUtil.isEmpty(keyPrefix)) {
            return "key prefix is empty";
        }
        Set<String> cacheKeys = redisClient.keysByPrefix(keyPrefix);
        LogHelp.warn(log, "removeByKeyPrefix accepted, prefix={}, keys={}", keyPrefix, cacheKeys.size());
        for (String cacheKey : cacheKeys) {
            redisClient.delete(cacheKey);
        }
        return "success, removed=" + cacheKeys.size();
    }

    /**
     * 令牌校验：优先读环境变量，其次读同名系统属性；二者均未配置时拒绝所有危险操作。
     * 比较使用常量时间方式，避免通过响应耗时逐字符猜测令牌。
     */
    private String validateToken(String token) {
        String expected = System.getenv(JMX_TOKEN_KEY);
        if (StrUtil.isBlank(expected)) {
            expected = System.getProperty(JMX_TOKEN_KEY);
        }
        if (StrUtil.isBlank(expected)) {
            return "JMX token is not configured; operation disabled";
        }
        if (StrUtil.isBlank(token)) {
            return "token is empty";
        }
        byte[] expectedBytes = expected.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = token.getBytes(StandardCharsets.UTF_8);
        if (!MessageDigest.isEqual(expectedBytes, actualBytes)) {
            return "token is not valid";
        }
        return null;
    }
}
