package io.gitee.dqcer.mcdull.uac.provider.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * git属性组件
 *
 * @author dqcer
 * @since 2024/05/31
 */
@Lazy
@Component
public class GitPropertiesComponent {

    private final Properties properties = new Properties();

    public GitPropertiesComponent() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("git.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("git.properties not found in the classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load git.properties", e);
        }
    }

    public String getCommitId() {
        return properties.getProperty("git.commit.id");
    }

}