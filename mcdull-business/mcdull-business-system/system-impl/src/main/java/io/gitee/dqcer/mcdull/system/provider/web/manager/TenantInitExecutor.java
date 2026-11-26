package io.gitee.dqcer.mcdull.system.provider.web.manager;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.system.provider.config.TenantInitProperties;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DatasourceEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantInitTaskEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.TenantInitStatusEnum;
import io.gitee.dqcer.mcdull.system.provider.model.enums.TenantInitStepEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantInitProgressVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantTaskVO;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.DatasourceMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantInitTaskMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class TenantInitExecutor {

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private DatasourceMapper datasourceMapper;
    @Resource
    private TenantInitTaskMapper taskMapper;
    @Resource
    private DatasourceCryptoManager cryptoManager;
    @Resource
    private TenantInitProgressPublisher publisher;
    @Resource
    private TenantInitProperties properties;

    public TenantTaskVO start(Integer tenantId, Boolean force) {
        TenantEntity tenant = tenantMapper.selectById(tenantId);
        if (tenant == null || Boolean.TRUE.equals(tenant.getDelFlag())) {
            throw new BusinessException("system.tenant.not.exist");
        }
        if (!TenantInitStatusEnum.SUCCESS.getCode().equals(tenant.getInitStatus()) && Boolean.TRUE.equals(force)) {
            throw new BusinessException("system.tenant.init.force.invalid");
        }
        LambdaQueryWrapper<TenantInitTaskEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantInitTaskEntity::getTenantId, tenantId)
             .eq(TenantInitTaskEntity::getStatus, 0)
             .eq(TenantInitTaskEntity::getDelFlag, false);
        if (taskMapper.selectCount(query) > 0) {
            throw new BusinessException("system.tenant.init.running");
        }

        String taskNo = "init-" + tenantId + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String databaseName = "tenant_" + tenantId;
        TenantInitTaskEntity task = new TenantInitTaskEntity();
        task.setTaskNo(taskNo);
        task.setTenantId(tenantId);
        task.setDatasourceId(tenant.getDatasourceId());
        task.setDatabaseName(databaseName);
        task.setStatus(0);
        task.setProgress(0);
        task.setCurrentStepCode(TenantInitStepEnum.DB_EXISTS_CHECK.getCode());
        task.setDatabaseCreated(false);
        task.setForceFlag(Boolean.TRUE.equals(force));
        task.setApplicationCodes(tenant.getApplicationCode());
        task.setScriptStatuses("{}");
        task.setStartedTime(new Date());
        taskMapper.insert(task);

        TenantEntity updating = new TenantEntity();
        updating.setId(tenantId);
        updating.setInitStatus(TenantInitStatusEnum.RUNNING.getCode());
        tenantMapper.updateById(updating);

        TenantTaskVO result = new TenantTaskVO();
        result.setTaskNo(taskNo);
        result.setTenantId(tenantId);
        result.setDatabaseName(databaseName);
        result.setInitStatus(TenantInitStatusEnum.RUNNING.getCode());
        result.setCurrentStepCode(TenantInitStepEnum.DB_EXISTS_CHECK.getCode());
        CompletableFuture.runAsync(() -> execute(taskNo));
        return result;
    }

    public TenantInitProgressVO snapshot(String taskNo) {
        TenantInitTaskEntity task = findTask(taskNo);
        TenantInitProgressVO vo = new TenantInitProgressVO();
        vo.setTenantId(task.getTenantId());
        vo.setTaskNo(taskNo);
        vo.setTaskId(taskNo);
        vo.setStepCode(task.getCurrentStepCode());
        vo.setStepName(stepName(task.getCurrentStepCode()));
        vo.setProgress(task.getProgress());
        vo.setDatabaseName(task.getDatabaseName());
        vo.setDatabaseCreated(task.getDatabaseCreated());
        vo.setTimestamp(System.currentTimeMillis());
        return vo;
    }

    private void execute(String taskNo) {
        TenantInitTaskEntity task = findTask(taskNo);
        try {
            TenantEntity tenant = tenantMapper.selectById(task.getTenantId());
            DatasourceEntity datasource = datasourceMapper.selectById(task.getDatasourceId());
            if (tenant == null || datasource == null) {
                fail(taskNo, TenantInitStepEnum.DB_EXISTS_CHECK.getCode(), "system.tenant.not.exist");
                return;
            }
            String password = cryptoManager.decrypt(datasource.getPasswordCipher());
            String serverUrl = serverUrl(datasource.getJdbcUrl());

            progress(task, TenantInitStepEnum.DB_EXISTS_CHECK, 10, "预检目标库 " + task.getDatabaseName());
            boolean exists = databaseExists(serverUrl, datasource.getUsername(), password, task.getDatabaseName());
            if (exists && !Boolean.TRUE.equals(task.getDatabaseCreated()) && !Boolean.TRUE.equals(task.getForceFlag())) {
                fail(taskNo, TenantInitStepEnum.DB_EXISTS_CHECK.getCode(), "system.tenant.database.exists");
                return;
            }
            progress(task, TenantInitStepEnum.DB_EXISTS_CHECK, 15, exists ? "目标库已存在，跳过建库" : "目标库不存在，预检通过");

            if (!exists) {
                progress(task, TenantInitStepEnum.CREATE_DATABASE, 30, "创建数据库 " + task.getDatabaseName());
                createDatabase(serverUrl, datasource.getUsername(), password, task.getDatabaseName());
                task.setDatabaseCreated(true);
                updateTask(task);
                progress(task, TenantInitStepEnum.CREATE_DATABASE, 35, "CREATE DATABASE 完成");
            }

            List<String> apps = JSON.parseArray(task.getApplicationCodes(), String.class);
            Map<String, String> scriptStatuses = new HashMap<>();
            if (apps != null) {
                int index = 0;
                for (String app : apps) {
                    index++;
                    int percent = 35 + (50 / Math.max(apps.size(), 1)) * index;
                    progress(task, TenantInitStepEnum.APP_SCRIPT, percent, "执行 " + app + " 初始化脚本");
                    executeScript(datasource.getJdbcUrl(), datasource.getUsername(), password, task.getDatabaseName(), app);
                    scriptStatuses.put(app, "success");
                    task.setScriptStatuses(JSON.toJSONString(scriptStatuses));
                    updateTask(task);
                }
            }

            TenantEntity updating = new TenantEntity();
            updating.setId(task.getTenantId());
            updating.setDatabaseName(task.getDatabaseName());
            updating.setInitStatus(TenantInitStatusEnum.SUCCESS.getCode());
            tenantMapper.updateById(updating);

            task.setStatus(1);
            task.setProgress(100);
            task.setFinishedTime(new Date());
            task.setCurrentStepCode(TenantInitStepEnum.DONE.getCode());
            updateTask(task);
            TenantInitProgressVO vo = progress(task, TenantInitStepEnum.DONE, 100, "初始化成功");
            publisher.publish(taskNo, "success", vo);
            publisher.publish(taskNo, "done", vo);
        } catch (Exception e) {
            fail(taskNo, task.getCurrentStepCode(), "system.tenant.init.failed");
        } finally {
            publisher.closeTask(taskNo);
        }
    }

    private void executeScript(String jdbcUrl, String username, String password, String databaseName, String app) throws Exception {
        Path script = Paths.get(properties.getInitScriptDir(), app, "schema.sql");
        if (!Files.exists(script)) {
            throw new BusinessException("system.tenant.init.script.missing");
        }
        String content = Files.readString(script, StandardCharsets.UTF_8);
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute("USE `" + databaseName + "`");
            for (String sql : content.split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                    statement.execute(trimmed);
                }
            }
        }
    }

    private boolean databaseExists(String serverUrl, String username, String password, String databaseName) throws Exception {
        try (Connection connection = DriverManager.getConnection(serverUrl, username, password);
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?")) {
            ps.setString(1, databaseName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void createDatabase(String serverUrl, String username, String password, String databaseName) throws Exception {
        try (Connection connection = DriverManager.getConnection(serverUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + databaseName + "`");
        }
    }

    private String serverUrl(String jdbcUrl) {
        String base = jdbcUrl;
        String query = "";
        int q = base.indexOf('?');
        if (q > 0) {
            query = base.substring(q);
            base = base.substring(0, q);
        }
        int slash = base.lastIndexOf('/');
        if (slash > "jdbc:mysql://".length()) {
            base = base.substring(0, slash);
        }
        return base + query;
    }

    private TenantInitTaskEntity findTask(String taskNo) {
        LambdaQueryWrapper<TenantInitTaskEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantInitTaskEntity::getTaskNo, taskNo);
        TenantInitTaskEntity task = taskMapper.selectOne(query);
        if (task == null) {
            throw new BusinessException("system.tenant.init.task.not.exist");
        }
        return task;
    }

    private void updateTask(TenantInitTaskEntity task) {
        taskMapper.updateById(task);
    }

    private TenantInitProgressVO progress(TenantInitTaskEntity task, TenantInitStepEnum step, int percent, String message) {
        task.setCurrentStepCode(step.getCode());
        task.setProgress(percent);
        updateTask(task);
        TenantInitProgressVO vo = new TenantInitProgressVO();
        vo.setTenantId(task.getTenantId());
        vo.setTaskNo(task.getTaskNo());
        vo.setTaskId(task.getTaskNo());
        vo.setStepCode(step.getCode());
        vo.setStepName(step.getText());
        vo.setProgress(percent);
        vo.setMessage(message);
        vo.setDatabaseName(task.getDatabaseName());
        vo.setDatabaseCreated(task.getDatabaseCreated());
        vo.setTimestamp(System.currentTimeMillis());
        publisher.publish(task.getTaskNo(), "step", vo);
        publisher.publish(task.getTaskNo(), "progress", vo);
        publisher.publish(task.getTaskNo(), "log", vo);
        return vo;
    }

    private void fail(String taskNo, String stepCode, String errorCode) {
        TenantInitTaskEntity task = findTask(taskNo);
        task.setStatus(2);
        task.setErrorCode(errorCode);
        task.setErrorMessage(errorCode);
        task.setFinishedTime(new Date());
        updateTask(task);
        TenantEntity updating = new TenantEntity();
        updating.setId(task.getTenantId());
        updating.setInitStatus(TenantInitStatusEnum.FAILED.getCode());
        tenantMapper.updateById(updating);
        TenantInitProgressVO vo = new TenantInitProgressVO();
        vo.setTenantId(task.getTenantId());
        vo.setTaskNo(taskNo);
        vo.setTaskId(taskNo);
        vo.setStepCode(stepCode);
        vo.setStepName(stepName(stepCode));
        vo.setFailedStepCode(stepCode);
        vo.setErrorCode(errorCode);
        vo.setMessage(errorCode);
        vo.setDatabaseName(task.getDatabaseName());
        vo.setProgress(task.getProgress());
        vo.setTimestamp(System.currentTimeMillis());
        publisher.publish(taskNo, "fail", vo);
        publisher.publish(taskNo, "done", vo);
    }

    private String stepName(String code) {
        return TenantInitStepEnum.DONE.getCode().equals(code) ? TenantInitStepEnum.DONE.getText()
                : TenantInitStepEnum.DB_EXISTS_CHECK.getCode().equals(code) ? TenantInitStepEnum.DB_EXISTS_CHECK.getText()
                : TenantInitStepEnum.CREATE_DATABASE.getCode().equals(code) ? TenantInitStepEnum.CREATE_DATABASE.getText()
                : TenantInitStepEnum.APP_SCRIPT.getCode().equals(code) ? TenantInitStepEnum.APP_SCRIPT.getText()
                : TenantInitStepEnum.WRITE_BACK.getCode().equals(code) ? TenantInitStepEnum.WRITE_BACK.getText()
                : "初始化";
    }
}
