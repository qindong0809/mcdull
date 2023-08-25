package io.gitee.dqcer.mcdull.admin.util.dump;


import io.gitee.dqcer.mcdull.admin.util.dump.util.SqlQueryUtility;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Dump create database statement.
 *
 * @author Elyar Adil
 * @since 1.0
 */
public class DatabaseDumper extends Dumper {
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
        printWriter.println(createStatement);
    }
}