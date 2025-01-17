package io.gitee.dqcer.mcdull.framework.web.jmx;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库 jmx
 *
 * @author dqcer
 * @since 2024/03/27
 */
@Component
@ManagedResource(
        objectName = "Customization:name=io.gitee.dqcer.mcdull.framework.web.jmx.DatabaseJmxAdapter",
        description = "Database util")
public class DatabaseJmxAdapter {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private DataSource dataSource;

    @ManagedOperation(description = "Execute SQL")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "password", description = "password"),
            @ManagedOperationParameter(name = "desc", description = "desc"),
            @ManagedOperationParameter(name = "file", description = "file")
    })
    public String executeSql(@RequestParam("password") String password,
                             @RequestParam("desc") String desc,
                             @RequestParam("file") MultipartFile file) throws IOException {
        log.warn("executeSql desc:{}", desc);
        String message = null;
        // todo 前置验证
        String sql = new String(file.getBytes());
        try (Connection connection = dataSource.getConnection()) {
            if (password != null) {
                connection.setAutoCommit(false);
                ScriptUtils.executeSqlScript(connection, new ByteArrayResource(sql.getBytes()));
                connection.commit();
                message = "success";
                // todo 后置入库记录
            }
        } catch (Exception e) {
            message = e.getMessage();
            log.error("executeSql error", e);
        }
        return message;
    }

    @ManagedOperation(description = "Query SQL")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "password", description = "password"),
            @ManagedOperationParameter(name = "desc", description = "desc"),
            @ManagedOperationParameter(name = "SQL", description = "SQL")
    })
    public String querySql(@RequestParam("password") String password,
                           @RequestParam("desc") String desc,
                           @RequestParam("sql") String sql) throws SQLException {
        log.warn("querySql desc:{}", desc);
        String message = null;
        // todo 前置验证
        List<Map<String, Object>> mapList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            if (password != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>(columnCount);
                    for (int i = 1; i <= columnCount; ++i) {
                        Object object = resultSet.getObject(i);
                        int columnType = metaData.getColumnType(i);
                        if (columnType == Types.TIME
                                || columnType == Types.TIMESTAMP
                                || columnType == Types.DATE
                                || columnType == Types.TIME_WITH_TIMEZONE
                                || columnType == Types.TIMESTAMP_WITH_TIMEZONE) {
                            Date date = Convert.toDate(object);
                            map.put(metaData.getColumnName(i), LocalDateTimeUtil.of(date).toString());
                        } else {
                            map.put(metaData.getColumnName(i), object);
                        }
                    }
                    mapList.add(map);
                }
            }
        }
        JSONArray objects = JSONUtil.parseArray(mapList);
        return objects.toString();
    }
}
