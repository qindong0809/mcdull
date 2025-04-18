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
        if (logDirectoryPath == null) {
            throw new ExceptionInInitializerError("环境变量为空：" + LOG_DIRECTORY);
        }
        System.setProperty(LOG_DIRECTORY, logDirectoryPath);

        // 1、从jar中的ip2region.xdb文件复制到服务器目录中
        File logDirectoryFile = new File(logDirectoryPath);
        if (!logDirectoryFile.exists()) {
            logDirectoryFile.mkdirs();
        }

        String tempFilePath;
        if (logDirectoryPath.endsWith(StrUtil.SLASH) || logDirectoryPath.endsWith(StrUtil.BACKSLASH)) {
            tempFilePath = logDirectoryPath + IP_FILE_NAME;
        } else {
            tempFilePath = logDirectoryPath + File.separator + IP_FILE_NAME;
        }

        File tempFile = new File(tempFilePath);
        try {
            Resource resourceObj = ResourceUtil.getResourceObj(IP_FILE_NAME);
            resourceObj.readBytes();
            FileUtil.writeBytes(resourceObj.readBytes(), tempFile);
            Ip2RegionUtil.init(tempFilePath);

        } catch (Exception e) {
            LogHelp.error(log, "无法复制ip数据文件 ip2region.xdb", e);
            throw new ExceptionInInitializerError("无法复制ip数据文件");
        } finally {
            if (tempFile.exists()) {
                boolean delete = tempFile.delete();
                if (!delete) {
                    LogHelp.error(log, "无法删除临时文件");
                }
            }
        }

    }


}