package io.gitee.dqcer.mcdull.business.common.dump;


import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.business.common.dump.util.SqlQueryUtility;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Dump create database statement.
 *
 * @author dqcer
 * @since 2024/11/18
 */
public class DatabaseDumper extends Dumper {

    private static final String DROP_DATABASE_TEMPLATE = "DROP DATABASE IF EXISTS `%s`;";

    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public DatabaseDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump database create statement
     *
     * @param databaseName name of the database
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void dump(String databaseName) throws SQLException {
        String sql = String.format("SHOW CREATE DATABASE IF NOT EXISTS `%s`", databaseName);
        String createStatement = SqlQueryUtility.queryString(connection, sql, 2);
        createStatement = StrUtil.format("{};", createStatement);
        printWriter.println(String.format(DROP_DATABASE_TEMPLATE, databaseName));
        printWriter.println(createStatement);
    }
}