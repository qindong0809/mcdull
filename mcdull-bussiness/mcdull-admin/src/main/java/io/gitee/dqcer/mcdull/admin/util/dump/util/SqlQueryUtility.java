package io.gitee.dqcer.mcdull.admin.util.dump.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Sql query utility class.
 *
 * @author Elyar Adil
 * @since 1.0
 */
public class SqlQueryUtility {
    /**
     * Get string from database.
     *
     * @param connection  sql connection
     * @param sql         sql statement
     * @param columnIndex index of column to retrieve string
     * @return string to be queried.
     * @throws SQLException if a database access error occurs
     */
    public static String queryString(Connection connection, String sql, int columnIndex) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        // First column is same as tableName, create statement is at second column.
        String createStatement = resultSet.getString(columnIndex);
        resultSet.close();
        statement.close();
        return createStatement;
    }

    /**
     * Query list of string.
     *
     * @param connection  sql connection
     * @param sql         sql statement
     * @param columnIndex index of column to retrieve string
     * @return list of string
     * @throws SQLException if a database access error occurs
     */
    public static List<String> queryStringList(Connection connection, String sql, int columnIndex) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<String> resultList = getStringListFromResultSet(connection, resultSet, columnIndex);
        resultSet.close();
        statement.close();
        return resultList;
    }

    /**
     * Retrieve list of string from resultSet.
     *
     * @param connection  sql connection
     * @param resultSet   where to retrieve string
     * @param columnIndex index of column to retrieve string
     * @return list of string
     * @throws SQLException if a database access error occurs
     */
    private static List<String> getStringListFromResultSet(Connection connection, ResultSet resultSet, int columnIndex) throws SQLException {
        List<String> stringList = new ArrayList<>();
        while (resultSet.next()) {
            String table = resultSet.getString(columnIndex);
            stringList.add(table);
        }
        return stringList;
    }

    /**
     * Return list of column names from given {@code ResultSet}
     *
     * @param resultSet the {@code ResultSet} to retrieve column names from
     * @return {@code List<String>} contains column names
     * @throws SQLException if a database access error occurs
     */
    private List<String> columnName(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        // set initialCapacity to column count
        List<String> columnNameList = new ArrayList<>(resultSetMetaData.getColumnCount());

        // column index starts from 1
        for (int columnIndex = 1; columnIndex <= resultSetMetaData.getColumnCount(); columnIndex++) {
            columnNameList.add(resultSetMetaData.getColumnName(columnIndex));
        }
        return columnNameList;
    }

    /**
     * Select database.
     *
     * @param connection sql connection
     * @param database   name of database to be selected
     * @throws SQLException if a database access error occurs
     */
    public static void selectDatabase(Connection connection, String database) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = String.format("USE `%s`", database);
        statement.execute(sql);
        statement.close();
    }
}
