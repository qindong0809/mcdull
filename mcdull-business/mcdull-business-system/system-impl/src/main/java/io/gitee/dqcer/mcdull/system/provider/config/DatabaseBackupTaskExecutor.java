package io.gitee.dqcer.mcdull.system.provider.config;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.TaskInstance;

import io.gitee.dqcer.mcdull.common.dump.SqlDumper;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.mysql.datasource.SwitchableDataSource;
import io.gitee.dqcer.mcdull.framework.scheduler.node.BackoffStrategy;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduleDefinition;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduledTask;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFolderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;

/**
 * 数据库备份任务执行程序。
 *
 * <p>cron 表达式来自系统配置项 {@code task-database-backup}，应用启动时读取并注册到 db-scheduler。
 * 集群下的单实例执行由 db-scheduler 的数据库行锁保证，不再需要额外的分布式锁。
 *
 * @author dqcer
 * @since 2024/11/18
 */
@Slf4j
@Component
public class DatabaseBackupTaskExecutor implements McdullScheduledTask<Void> {

    /**
     * 任务名称（同时作为系统配置项的 key）
     */
    public static final String TASK_NAME = "task-database-backup";

    /**
     * 配置缺失时的兜底 cron（每天凌晨 2 点）
     */
    private static final String DEFAULT_CRON = "0 0 2 * * ?";

    @Resource
    private IFileService fileService;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IFolderService folderService;
    @Resource
    private SwitchableDataSource globalDataRoutingDataSource;

    @Override
    public McdullScheduleDefinition definition() {
        String cron;
        try {
            globalDataRoutingDataSource.switchDataSource();
            cron = commonManager.getConfig(TASK_NAME);
            log.info("{}: {}", TASK_NAME, cron);
        } finally {
            globalDataRoutingDataSource.removeDataSource();
        }

        boolean enabled = StrUtil.isNotBlank(cron);
        return McdullScheduleDefinition.builder()
                .name(TASK_NAME)
                .cron(enabled ? cron : DEFAULT_CRON)
                .enabled(enabled)
                .maxRetries(0)
                .backoff(BackoffStrategy.NONE)
                .build();
    }

    @Override
    public void execute(TaskInstance<Void> instance, ExecutionContext context) {
        startBackup();
    }

    private void startBackup() {
        try {
            globalDataRoutingDataSource.switchDataSource();
            File file = null;
            File zipFile = null;
            try (Connection connection = globalDataRoutingDataSource.getConnection()) {
                UserContextHolder.setDefaultSession();
                UnifySession session = UserContextHolder.getSession();
                if (session.getTraceId() == null) {
                    session.setTraceId(RandomUtil.uuid());
                }
                String schema = connection.getCatalog();
                LogHelp.info(log, "start backup database. schema:{}", schema);
                String tmpDirPath = FileUtil.getTmpDirPath();
                String dateTimeStr = commonManager.convertDateTimeStr(new Date());
                String fileName = schema + "_" + dateTimeStr + ".sql";
                file = SqlDumper.dumpDatabase(connection, new HashSet<>(ListUtil.of(schema)), tmpDirPath + File.separator + fileName);
                zipFile = ZipUtil.zip(file);
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
        } finally {
            globalDataRoutingDataSource.removeDataSource();
        }
    }
}
