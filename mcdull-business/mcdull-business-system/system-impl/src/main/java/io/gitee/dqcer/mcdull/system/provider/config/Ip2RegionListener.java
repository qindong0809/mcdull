package io.gitee.dqcer.mcdull.system.provider.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
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
        String logDirectoryPath = environment.getProperty(LOG_DIRECTORY);
        if (StrUtil.isBlank(logDirectoryPath)) {
            logDirectoryPath = "./logs";
        }

        // 目录不可创建时降级到 JVM 临时目录，避免 ip2region 初始化阻断服务启动
        File logDirectoryFile = new File(logDirectoryPath);
        if (!logDirectoryFile.exists() && !logDirectoryFile.mkdirs()) {
            log.warn("无法创建日志目录 {}，ip2region.xdb 改用临时目录", logDirectoryPath);
            logDirectoryPath = System.getProperty("java.io.tmpdir");
        }
        System.setProperty(LOG_DIRECTORY, logDirectoryPath);

        String tempFilePath;
        if (logDirectoryPath.endsWith(StrUtil.SLASH) || logDirectoryPath.endsWith(StrUtil.BACKSLASH)) {
            tempFilePath = logDirectoryPath + IP_FILE_NAME;
        } else {
            tempFilePath = logDirectoryPath + File.separator + IP_FILE_NAME;
        }

        File tempFile = new File(tempFilePath);
        try {
            Resource resourceObj = ResourceUtil.getResourceObj(IP_FILE_NAME);
            FileUtil.writeBytes(resourceObj.readBytes(), tempFile);
            Ip2RegionUtil.init(tempFilePath);
            log.info("ip2region.xdb 初始化完成，路径: {}", tempFilePath);
        } catch (Exception e) {
            // IP 归属地解析失败时返回空结果，不影响服务启动与核心业务
            LogHelp.error(log, "无法复制ip数据文件 ip2region.xdb，IP 归属地能力将不可用", e);
        } finally {
            if (tempFile.exists() && !tempFile.delete()) {
                LogHelp.error(log, "无法删除临时文件");
            }
        }

    }


}