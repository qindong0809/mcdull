package io.gitee.mcdull.tools.web.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import io.gitee.dqcer.mcdull.business.common.dump.MysqlUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.mcdull.tools.web.domain.DatabaseImportDTO;
import io.gitee.mcdull.tools.web.domain.ImportPhaseEnum;
import io.gitee.mcdull.tools.web.domain.ImportProgressVO;
import io.gitee.mcdull.tools.web.manager.DatabaseImportTaskManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Import a mysql dump into a freshly created versioned schema and then apply masking sql.
 * <p>
 * Every import creates its own schema named {@code <source>_<timestamp>}, so no existing database is
 * ever written to. A failure at any point only requires dropping that schema, which this tool
 * created itself, so cleanup has no blast radius. Older versions are pruned by a retention count.
 *
 * @author dqcer
 */
@Slf4j
@Service
public class DatabaseImportService {

    private static final String SQL_SUFFIX = ".sql";

    /**
     * Mysql identifier limit, the generated schema name must fit into it.
     */
    private static final int MAX_IDENTIFIER_LENGTH = 64;

    /**
     * Database names are read out of an uploaded file and end up inside back quoted identifiers of
     * CREATE and DROP statements, so anything outside this set is rejected rather than escaped.
     */
    private static final Pattern SAFE_IDENTIFIER = Pattern.compile("[A-Za-z0-9_$]{1,64}");

    /**
     * The USE statement sits in the dump header, there is no reason to scan further and no reason to
     * read a multi gigabyte file just to report that it is missing.
     */
    private static final int HEADER_SCAN_LINE_LIMIT = 500;

    private static final DateTimeFormatter VERSION_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Maximum time to wait for the mysql client to finish the import.
     */
    private static final long IMPORT_TIMEOUT_MINUTES = 30L;

    @Resource
    private DatabaseImportTaskManager databaseImportTaskManager;

    @Value("${mysql.client.dump-path:C:\\Program Files\\MySQL\\MySQL Workbench 8.0}")
    private String dumpPath;

    /**
     * How many versioned schemas of the same source database are kept.
     */
    @Value("${mysql.import.keep-versions:3}")
    private int keepVersions;

    /**
     * Persist the upload, create the versioned schema and start the import on a worker thread.
     *
     * @param file the mysql dump file
     * @param dto  target connection info and optional masking sql
     * @return id of the created task, used to poll progress
     */
    public String submitImport(MultipartFile file, DatabaseImportDTO dto) {
        // Validate the suffix before writing anything to disk, and fail loudly instead of
        // silently returning success for unsupported files.
        if (!StrUtil.endWithIgnoreCase(file.getOriginalFilename(), SQL_SUFFIX)) {
            throw new BusinessException("Only .sql dump file is supported");
        }
        String baseName = String.join(File.separator, GlobalConstant.TMP_DIR,
                GlobalConstant.ROOT_PREFIX, Long.toString(System.currentTimeMillis()));
        File sqlFile = FileUtil.touch(baseName + SQL_SUFFIX);
        File outputFile = FileUtil.touch(baseName + ".log");
        ImportProgressVO task = null;
        try {
            // The multipart stream must be consumed inside the request, so the file is written here
            // and only the local copy is used afterwards.
            file.transferTo(sqlFile);
            DumpHeader header = this.parseDumpHeader(sqlFile);
            String versionedName = this.buildVersionedName(header.database());
            task = databaseImportTaskManager.register(sqlFile.length());
            task.setSourceDatabaseName(header.database());
            task.setDatabaseName(versionedName);
            // Created up front so the dump's own CREATE DATABASE can be dropped while keeping the
            // original character set and collation.
            this.createDatabase(dto, versionedName, header.createSuffix());
            this.runAsync(sqlFile, outputFile, dto, task);
            return task.getTaskId();
        } catch (IOException e) {
            this.cleanUpFiles(sqlFile, outputFile);
            LogHelp.error(log, "failed to store the upload. file: {}", file.getOriginalFilename(), e);
            throw new BusinessException("Failed to store the uploaded dump file", e);
        } catch (BusinessException e) {
            // Nothing was handed to a worker, clean up here. On the success path the worker owns
            // these files and deletes them when it finishes.
            this.cleanUpFiles(sqlFile, outputFile);
            if (task != null) {
                databaseImportTaskManager.complete(task, ImportPhaseEnum.FAILED, e.getMessage());
            }
            throw e;
        }
    }

    /**
     * Read the current progress of a task.
     *
     * @param taskId task id returned by the submit call
     * @return current progress
     */
    public ImportProgressVO getProgress(String taskId) {
        ImportProgressVO task = databaseImportTaskManager.get(taskId);
        if (task == null) {
            throw new BusinessException("Unknown or expired import task: " + taskId);
        }
        return task;
    }

    private void runAsync(File sqlFile, File outputFile, DatabaseImportDTO dto, ImportProgressVO task) {
        databaseImportTaskManager.submit(() -> {
            try {
                this.runImport(sqlFile, outputFile, dto, task);
                task.setPhase(ImportPhaseEnum.MASKING);
                String maskLog = this.runMaskSql(dto, task.getDatabaseName());
                task.setPercent(100);
                databaseImportTaskManager.complete(task, ImportPhaseEnum.SUCCESS, maskLog);
                this.applyRetention(dto, task.getSourceDatabaseName(), task.getDatabaseName());
            } catch (BusinessException e) {
                this.abandonDatabase(dto, task, e.getMessage());
            } catch (Exception e) {
                // Top level of the worker thread: anything not caught here would be swallowed by the
                // pool and leave the task stuck in a running phase forever.
                LogHelp.error(log, "import task crashed. taskId: {}", task.getTaskId(), e);
                this.abandonDatabase(dto, task, e.getMessage());
            } finally {
                this.cleanUpFiles(sqlFile, outputFile);
            }
        });
    }

    /**
     * Drop the versioned schema of a failed import so no partially imported or unmasked data is
     * left behind. The schema was created by this tool, so dropping it cannot affect anything else.
     */
    private void abandonDatabase(DatabaseImportDTO dto, ImportProgressVO task, String reason) {
        String versionedName = task.getDatabaseName();
        String message = reason;
        try {
            MysqlUtil.runSql(dto.getIp(), dto.getPort(), dto.getUsername(), dto.getPassword(),
                    StrUtil.EMPTY, StrUtil.format("DROP DATABASE IF EXISTS `{}`;", versionedName));
        } catch (Exception e) {
            LogHelp.error(log, "failed to drop the failed schema. database: {}", versionedName, e);
            message = reason + " | WARNING: schema " + versionedName
                    + " could not be dropped and may hold unmasked data";
        }
        databaseImportTaskManager.complete(task, ImportPhaseEnum.FAILED, message);
    }

    /**
     * Run the mysql client and stream the dump into it.
     */
    private void runImport(File sqlFile, File outputFile, DatabaseImportDTO dto, ImportProgressVO task) {
        // Use a local variable, the injected field must not be mutated on this singleton bean.
        String clientDir = dumpPath.replace("\\", "/").replace("%", "");
        // Pass every argument as a separate element: a client path containing spaces is no longer
        // split into tokens, and user input can no longer inject extra mysql options.
        ProcessBuilder builder = new ProcessBuilder(
                clientDir + "/mysql",
                "-h" + dto.getIp(),
                "-P" + dto.getPort(),
                "-u" + dto.getUsername(),
                "--default-character-set=utf8");
        // Pass the password by environment variable so it does not show up in the process list.
        builder.environment().put("MYSQL_PWD", dto.getPassword());
        // Capture the client output in a file, which also removes any risk of the output pipe
        // filling up and deadlocking against the dump being written to stdin.
        builder.redirectErrorStream(true);
        builder.redirectOutput(outputFile);

        Process process = null;
        try {
            process = builder.start();
            this.feedDump(sqlFile, process, task);
            // The client must be allowed to finish. Destroying it right after start truncates
            // the import and leaves the schema in a partial state.
            if (!process.waitFor(IMPORT_TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
                throw new BusinessException("Data import timed out after "
                        + IMPORT_TIMEOUT_MINUTES + " minutes");
            }
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                String output = FileUtil.readUtf8String(outputFile);
                LogHelp.error(log, "data import failed. exitCode: {}, output: {}", exitCode, output);
                throw new BusinessException("Data import failed, mysql client exit code "
                        + exitCode + ": " + StrUtil.brief(output, 500));
            }
        } catch (IOException e) {
            LogHelp.error(log, "failed to start mysql client. clientDir: {}", clientDir, e);
            throw new BusinessException("Failed to start mysql client", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("Data import was interrupted", e);
        } finally {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    /**
     * Stream the dump into the client stdin, redirecting it at the versioned schema and counting
     * bytes so the task exposes real progress. Closing stdin sends EOF, which makes the client
     * finish and exit.
     */
    private void feedDump(File sqlFile, Process process, ImportProgressVO task) {
        long total = task.getTotalBytes();
        DumpStreamRewriter rewriter = new DumpStreamRewriter(task.getDatabaseName());
        try (InputStream input = Files.newInputStream(sqlFile.toPath());
             OutputStream stdin = process.getOutputStream()) {
            rewriter.rewriteAndCopy(input, stdin, read -> {
                task.setProcessedBytes(read);
                if (total > 0) {
                    task.setPercent((int) (read * 100L / total));
                }
            });
        } catch (IOException e) {
            // A broken pipe means the client already exited, normally because of a sql error. Do not
            // fail here: the caller reports the real reason from the exit code and captured output.
            LogHelp.warn(log, "stopped feeding dump after {} bytes: {}",
                    task.getProcessedBytes(), e.getMessage());
        }
    }

    /**
     * Run the optional masking sql against the imported schema.
     */
    private String runMaskSql(DatabaseImportDTO dto, String databaseName) {
        String sql = dto.getSql();
        if (StrUtil.isBlank(sql)) {
            return StrUtil.EMPTY;
        }
        try {
            return MysqlUtil.runSql(dto.getIp(), dto.getPort(), dto.getUsername(),
                    dto.getPassword(), databaseName, sql);
        } catch (Exception e) {
            LogHelp.error(log, "data masking failed. database: {}", databaseName, e);
            throw new BusinessException("Masking sql failed: " + e.getMessage(), e);
        }
    }

    /**
     * Build the versioned schema name, truncating the source name so the result fits the mysql
     * identifier limit.
     */
    private String buildVersionedName(String sourceDatabase) {
        String suffix = "_" + VERSION_FORMATTER.format(LocalDateTime.now());
        int room = MAX_IDENTIFIER_LENGTH - suffix.length();
        String prefix = sourceDatabase.length() > room ? sourceDatabase.substring(0, room) : sourceDatabase;
        return prefix + suffix;
    }

    /**
     * Create the versioned schema. A plain CREATE fails when the name already exists, which surfaces
     * a same second collision instead of importing into a schema someone else owns.
     */
    private void createDatabase(DatabaseImportDTO dto, String versionedName, String createSuffix) {
        // ScriptRunner splits on the semicolon, without it the statement would never be executed.
        String options = StrUtil.isBlank(createSuffix) ? StrUtil.EMPTY
                : StrUtil.addSuffixIfNot(createSuffix, ";");
        String statement = StrUtil.isBlank(options)
                ? StrUtil.format("CREATE DATABASE `{}`;", versionedName)
                : StrUtil.format("CREATE DATABASE `{}` {}", versionedName, options);
        try {
            MysqlUtil.runSql(dto.getIp(), dto.getPort(), dto.getUsername(), dto.getPassword(),
                    StrUtil.EMPTY, statement);
        } catch (Exception e) {
            LogHelp.error(log, "failed to create schema. database: {}", versionedName, e);
            throw new BusinessException("Failed to create schema " + versionedName
                    + ": " + e.getMessage(), e);
        }
    }

    /**
     * Drop the oldest versioned schemas of the same source database beyond the retention count.
     * Housekeeping must never fail the import that just succeeded.
     */
    private void applyRetention(DatabaseImportDTO dto, String sourceDatabase, String keepAtLeast) {
        if (keepVersions <= 0) {
            return;
        }
        try {
            Db db = MysqlUtil.getInstance(dto.getIp(), dto.getPort(), dto.getUsername(),
                    dto.getPassword(), StrUtil.EMPTY);
            List<Entity> rows = db.query("SELECT SCHEMA_NAME FROM information_schema.SCHEMATA");
            List<String> versions = new ArrayList<>();
            for (Entity row : rows) {
                String name = row.getStr("SCHEMA_NAME");
                // Filter in java rather than with LIKE so underscores need no escaping. The name is
                // re-validated because it is about to be interpolated into a DROP statement.
                if (name != null && name.startsWith(sourceDatabase + "_")
                        && SAFE_IDENTIFIER.matcher(name).matches()) {
                    versions.add(name);
                }
            }
            // The timestamp suffix has a fixed width, so lexical order equals chronological order.
            versions.sort(Comparator.reverseOrder());
            for (int index = keepVersions; index < versions.size(); index++) {
                String stale = versions.get(index);
                if (stale.equals(keepAtLeast)) {
                    continue;
                }
                db.execute(StrUtil.format("DROP DATABASE `{}`", stale));
                LogHelp.info(log, "pruned stale imported schema: {}", stale);
            }
        } catch (Exception e) {
            LogHelp.warn(log, "retention cleanup skipped for {}: {}", sourceDatabase, e.getMessage());
        }
    }

    /**
     * Read the dump header for the database it targets and the options of its CREATE DATABASE
     * statement.
     */
    private DumpHeader parseDumpHeader(File sqlFile) {
        String createSuffix = null;
        // InputStreamReader replaces malformed bytes instead of throwing, which matters because a
        // dump can hold escaped binary data that is not valid UTF-8.
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(sqlFile.toPath()), StandardCharsets.UTF_8))) {
            String line;
            int scanned = 0;
            while (scanned++ < HEADER_SCAN_LINE_LIMIT && (line = reader.readLine()) != null) {
                if (DumpStreamRewriter.isCreateDatabase(line)) {
                    createSuffix = DumpStreamRewriter.extractCreateDatabaseSuffix(line);
                    continue;
                }
                if (!DumpStreamRewriter.isUse(line)) {
                    continue;
                }
                String database = DumpStreamRewriter.parseUseTarget(line);
                if (StrUtil.isNotBlank(database)) {
                    return new DumpHeader(this.requireSafeIdentifier(database), createSuffix);
                }
            }
        } catch (IOException e) {
            LogHelp.error(log, "failed to read dump file. file: {}", sqlFile.getName(), e);
            throw new BusinessException("Failed to read the dump file", e);
        }
        throw new BusinessException("No 'USE <database>' statement found in the dump header, "
                + "export it with mysqldump --databases");
    }

    /**
     * Reject database names that cannot be safely embedded in a back quoted identifier.
     */
    private String requireSafeIdentifier(String database) {
        if (!SAFE_IDENTIFIER.matcher(database).matches()) {
            throw new BusinessException("Unsupported database name in dump, only letters, digits, "
                    + "underscore and dollar are allowed: " + StrUtil.brief(database, 64));
        }
        return database;
    }

    private void cleanUpFiles(File sqlFile, File outputFile) {
        // The dump still holds unmasked production data, never leave it on disk.
        FileUtil.del(sqlFile);
        FileUtil.del(outputFile);
    }

    /**
     * Header information extracted from a dump file.
     *
     * @param database     database the dump targets
     * @param createSuffix options of the dump's CREATE DATABASE statement, may be null
     */
    private record DumpHeader(String database, String createSuffix) {
    }
}
