package io.gitee.mcdull.tools.web.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.business.common.dump.MysqlUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.mcdull.tools.web.domain.DatabaseImportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@Service
public class DatabaseImportService {

    @Value("${mysql.client.dump-path:C:\\Program Files\\MySQL\\MySQL Workbench 8.0}")
    private String dumpPath;

    /**
     * 数据导入和脱敏处理
     *
     * @param file 文件
     * @return {@link String }
     */
    public Pair<String, String> dataImportAndMasker(MultipartFile file, DatabaseImportDTO dto) {
        String databaseName = this.getDatabaseName(file);
        String originalFilename = file.getOriginalFilename();
        String tempFile = String.join(File.separator, GlobalConstant.TMP_DIR, GlobalConstant.ROOT_PREFIX, Long.toString(System.currentTimeMillis()) + ".sql");
        try {
            file.transferTo(FileUtil.touch(tempFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (StrUtil.endWith(originalFilename, ".sql")) {
            dumpPath  = dumpPath.replaceAll("\\\\", "/").replaceAll("%", "");
            String cmd = StrUtil.format("{}/mysql -h{} -P{} -u{} -p{} --default-character-set=utf8",
                    dumpPath, dto.getIp(), dto.getPort(), dto.getUsername(), dto.getPassword());
            String sourceCmd = StrUtil.format("source {}", tempFile);
            OutputStream outputStream = null;
            Process process = null;
            OutputStreamWriter writer = null;
            try {
                Runtime runtime = Runtime.getRuntime();
                process = runtime.exec(cmd);
                outputStream = process.getOutputStream();
                writer = new OutputStreamWriter(outputStream);
                writer.write(sourceCmd);
                writer.flush();
            } catch (Exception e) {
                LogHelp.error(log, "date import error. sourceCmd: {}", sourceCmd, e);
                throw new RuntimeException(e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        LogHelp.error(log, "write close error. sourceCmd: {}", sourceCmd, e);
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LogHelp.error(log, "outputStream close error. sourceCmd: {}", sourceCmd, e);
                    }
                }
                if (process != null) {
                    process.destroy();
                }
            }
            String sql = dto.getSql();
            if (StrUtil.isNotBlank(sql)) {
                String runSqlLog = MysqlUtil.runSql(dto.getIp(), dto.getPort(), dto.getUsername(), dto.getPassword(), databaseName, sql);
                return Pair.of(databaseName, runSqlLog);
            }
        }
        return Pair.of(databaseName, "");
    }

    private String getDatabaseName(MultipartFile sqlFile) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sqlFile.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().toUpperCase().startsWith("USE ")) {
                    String databaseName = line.trim().substring(4).replaceAll("[;\\s]", "");
                    reader.close();
                    databaseName = databaseName.replaceAll("`", "");
                    return databaseName;
                }
            }
            reader.close();
            throw new IllegalArgumentException("The database name was not found");
        } catch (IOException e) {
            throw new IllegalArgumentException("The database name was not found");
        }
    }
}
