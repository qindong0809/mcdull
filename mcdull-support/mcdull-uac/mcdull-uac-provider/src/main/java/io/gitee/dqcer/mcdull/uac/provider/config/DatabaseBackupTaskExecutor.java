package io.gitee.dqcer.mcdull.uac.provider.config;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.cron.CronUtil;
import io.gitee.dqcer.mcdull.business.common.dump.SqlDumper;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.component.ConcurrentRateLimiter;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFolderService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;

/**
 * 数据库备份任务执行程序
 *
 * @author dqcer
 * @since 2024/11/18
 */
@Slf4j
@Component
public class DatabaseBackupTaskExecutor {

    @Resource
    private DataSource dataSource;
    @Resource
    private ConcurrentRateLimiter concurrentRateLimiter;
    @Resource
    private IFileService fileService;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IFolderService folderService;


    @PostConstruct
    public void init() {
        final String key = "task-database-backup";
        String value = commonManager.getConfig(key);
        log.info("task-database-backup: {}", value);
        if (StrUtil.isNotBlank(value)) {
            // 创建并注册定时任务
            CronUtil.schedule(value, (Runnable) () ->
                    concurrentRateLimiter.locker(key, 0, this::startBackup, false));
            // 秒级别定时任务
            CronUtil.setMatchSecond(true);
            // 启动定时任务调度器
            CronUtil.start();
        }
    }

    private boolean startBackup() {
        File file = null;
        File zipFile = null;
        try (Connection connection = dataSource.getConnection()) {
           String schema = connection.getCatalog();
            LogHelp.info(log, "start backup database. schema:{}", schema);
            String tmpDirPath = FileUtil.getTmpDirPath();
            String dateTimeStr = commonManager.convertDateTimeStr(new Date());
            String fileName = schema + "_" + dateTimeStr + ".sql";
            file = SqlDumper.dumpDatabase(connection, new HashSet<>(ListUtil.of(schema)), tmpDirPath + File.separator + fileName);
            zipFile = ZipUtil.zip(file);
            UserContextHolder.setDefaultSession();
            fileService.fileUpload(zipFile, folderService.addIfAbsent("数据备份", 0));
            LogHelp.info(log, "backup database success. fileName:{}", fileName);
        } catch (SQLException e) {
            log.error("backup database error", e);
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(file);
            FileUtil.del(zipFile);
            UserContextHolder.clearSession();
        }
        return true;
    }
}
