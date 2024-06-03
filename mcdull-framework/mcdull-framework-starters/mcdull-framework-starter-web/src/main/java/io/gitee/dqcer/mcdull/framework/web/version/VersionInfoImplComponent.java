package io.gitee.dqcer.mcdull.framework.web.version;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 版本信息
 *
 * @author dqcer
 * @since 2024/06/03
 */
@Component
public class VersionInfoImplComponent implements IVersionInfoComponent {

    private static final Logger log = LoggerFactory.getLogger(VersionInfoImplComponent.class);

    private static final String GIT_PROPERTIES = "git.properties";

    private static final String JAR_BUILD_PROPERTIES = "META-INF/build-info.properties";

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
            String prefix = "\"git.commit";
            if (key.startsWith(prefix)) {
                String value = (String) v;
                value = replaceFirstAndLast(value);
                key = replaceFirstAndLast(key);
                gitProperties.put(key, value);
            }
        });
        return gitProperties;
    }

    private static String replaceFirstAndLast(String str) {
        String prefix = "\"";
        if (str.startsWith(prefix)) {
            str = StrUtil.replaceFirst(str, "\"", "");
            str = StrUtil.replaceLast(str, "\"", "");
        }
        return str;
    }
}
