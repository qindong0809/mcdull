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
    public static final String JAR_BUILD_PROPERTIES = "META-INF/build-info.properties";

    @Override
    public Properties getJarCurrentBuildInfo() {
        return this.getProperties(JAR_BUILD_PROPERTIES);
    }

    @Override
    public Properties getGitCurrentCommitInfo() {
        Properties properties = this.getProperties(GIT_PROPERTIES);
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            properties.setProperty(key, value);
        }
        return this.gitCommitProperties(properties);
    }
    private Properties getProperties(String resourceName) {
        Properties properties = new Properties();
        try (InputStream inputStream = ResourceUtil.getStream(resourceName)) {
            if (inputStream != null) {
                properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            } else {
                LogHelp.error(log, "无法获取资源流", resourceName);
            }
        } catch (Exception e) {
            LogHelp.error(log, "获取版本信息失败", e);
        }
        return properties;
    }


    private Properties gitCommitProperties(Properties properties) {
        Properties gitProperties = new Properties();
        properties.forEach((k, v) -> {
            String key = (String) k;
            if (key.startsWith("\"git.commit")) {
                String value = (String) v;
                value = replaceFirstAndLast(value);
                key = replaceFirstAndLast(key);
                gitProperties.put(key, value);
            }
        });
        return gitProperties;
    }

    private static String replaceFirstAndLast(String str) {
        if (str.startsWith("\"")) {
            str = StrUtil.replaceFirst(str, "\"", "");
            str = StrUtil.replaceLast(str, "\"", "");
        }
        return str;
    }
}
