package io.gitee.dqcer.mcdull.system.provider.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.system.provider.util.Ip2RegionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;

/**
 * 初初始化ip工具类
 *
 * @author dqcer
 * @since 2024/06/18
 */
@Order(value = LoggingApplicationListener.DEFAULT_ORDER)
@Slf4j
public class Ip2RegionListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String IP_FILE_NAME = "ip2region.xdb";

    private static final String LOG_DIRECTORY = "logging.file.path";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEvent) {

        ConfigurableEnvironment environment = applicationEvent.getEnvironment();
        String logDirectoryPath = StrUtil.trim(environment.getProperty(LOG_DIRECTORY));
        if (StrUtil.isBlank(logDirectoryPath)) {
            logDirectoryPath = "./logs";
        }
        System.setProperty(LOG_DIRECTORY, logDirectoryPath);

        if (initIp2Region(buildPath(logDirectoryPath))) {
            return;
        }

        // 日志目录不可写时降级到 JVM 临时目录，避免 ip2region 初始化影响服务启动
        String tmpDirectory = StrUtil.trim(System.getProperty("java.io.tmpdir"));
        if (!StrUtil.equals(tmpDirectory, logDirectoryPath)
                && initIp2Region(buildPath(tmpDirectory))) {
            return;
        }
        log.warn("无法初始化 ip2region.xdb，IP 归属地能力将不可用");
    }

    private static String buildPath(String directory) {
        if (directory.endsWith(StrUtil.SLASH) || directory.endsWith(StrUtil.BACKSLASH)) {
            return directory + IP_FILE_NAME;
        }
        return directory + File.separator + IP_FILE_NAME;
    }

    private boolean initIp2Region(String filePath) {
        File tempFile = new File(filePath);
        File directory = tempFile.getParentFile();
        try {
            if (directory != null && directory.exists() && !directory.isDirectory()) {
                return false;
            }
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                return false;
            }
            Resource resourceObj = ResourceUtil.getResourceObj(IP_FILE_NAME);
            FileUtil.writeBytes(resourceObj.readBytes(), tempFile);
            Ip2RegionUtil.init(tempFile.getAbsolutePath());
            log.info("ip2region.xdb 初始化完成，路径: {}", tempFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            log.warn("在目录 {} 初始化 ip2region.xdb 失败: {}", directory, e.getMessage());
            return false;
        } finally {
            if (tempFile.exists() && !tempFile.delete()) {
                log.warn("无法删除临时文件 {}", tempFile.getAbsolutePath());
            }
        }
    }


}