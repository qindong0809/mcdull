package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author dqcer
 * @since 2024/05/23
 */
@Slf4j
@Service
public class VersionServiceImpl implements IVersionService {

    public static final String GIT_PROPERTIES = "git.properties";

    @Override
    public Properties getVersion() {
        Properties properties = new Properties();
        try (InputStream inputStream = ResourceUtil.getStream(GIT_PROPERTIES)) {
            if (inputStream != null) {
                properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            } else {
                LogHelp.error(log, "无法获取资源流", GIT_PROPERTIES);
            }
        } catch (Exception e) {
            LogHelp.error(log, "获取版本信息失败", e);
        }
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            properties.setProperty(key, value);
        }
        return this.gitCommitProperties(properties);
    }

    private Properties gitCommitProperties(Properties properties) {
        Properties gitProperties = new Properties();
        properties.forEach((k, v) -> {
            String key = (String) k;
            if (key.startsWith("\"git.commit")) {
                String value = (String) v;
                if (value.startsWith("\"")) {
                    value = StrUtil.replaceFirst(value, "\"", "");
                    value = StrUtil.replaceLast(value, "\"", "");
                }
                gitProperties.put(key, value);
            }
        });
        return gitProperties;
    }
}
