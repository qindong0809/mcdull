package io.gitee.dqcer.mcdull.frameowrk.soagov;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志级别配置
 * eg:
 * Data ID: log
 * 配置格式：properties
 * 配置内容（如下所示）：
 root=debug
 mybatis-plus=warn
 io.gitee=debug
 com.zaxxer.hikari=warn
 com.alibaba.nacos=error

 *
 * @author dqcer
 * @since 2022/10/24
 */
@Component
public class LogLevelListener implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(LogLevelListener.class);

    private static final String LOG_LEVEL_NAME = "log-level.properties";

    @Resource
    private NacosConfigManager nacosConfigManager;

    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @SuppressWarnings("all")
    @Override
    public void afterPropertiesSet() throws Exception {
        if (nacosConfigManager == null || nacosConfigProperties == null) {
            return;
        }

        new Thread(() -> {
            // 初次加载
            log.info("Loading Log Level File: {}", LOG_LEVEL_NAME);
            String configInfo = null;
            try {
                configInfo = nacosConfigManager.getConfigService().getConfig(LOG_LEVEL_NAME, nacosConfigProperties.getGroup(), 3000);
            } catch (NacosException e) {
                log.error("nacos exception", e);
            }
            updateLogLevelConfig(configInfo);

        }).start();


        // 二次监听加载
        nacosConfigManager.getConfigService().addListener(LOG_LEVEL_NAME, nacosConfigProperties.getGroup(), new Listener() {
            @Override
            public Executor getExecutor() {
                return new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500), r -> {
                    Thread thread = new Thread(r);
                    thread.setName("log-" + r.hashCode());
                    return thread;
                });
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                updateLogLevelConfig(configInfo);
            }
        });

    }

    private void updateLogLevelConfig(String configInfo) {
        if (null == configInfo || configInfo.trim().length() == 0) {
            return;
        }
        log.info("更新配置文件内容 \n{}", configInfo);
        Properties properties = new Properties();
        try {
            LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
            properties.load(new StringReader(configInfo));
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                Level level = Level.toLevel(entry.getValue().toString(), Level.INFO);
                loggerContext.getLogger(entry.getKey().toString()).setLevel(level);
                log.info("Update log level {}={}",entry.getKey(), level);
            }
        } catch (Exception e) {
            log.error("动态设置日志级别出现异常", e);
        }
    }
}
