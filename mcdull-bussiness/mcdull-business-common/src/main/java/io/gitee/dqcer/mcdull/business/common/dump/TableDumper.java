package io.gitee.dqcer.mcdull.business.common.dump;


import io.gitee.dqcer.mcdull.business.common.dump.util.SqlCommentUtility;
import io.gitee.dqcer.mcdull.business.common.dump.util.SqlQueryUtility;
import io.gitee.dqcer.mcdull.business.common.dump.util.SqlValueUtility;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to dump table structure and data.
 *
 * @author dqcer
 * @since 2024/11/18
 */
public class TableDumper extends Dumper {
    // comments structure SQL statement
    private static final String COMMENT_STRUCTURE = "Table structure for `%s`";
    // comments before insert statement
    private static final String COMMENT_RECORDS = "Data of table `%s`";

    public static final String STRING_NULL = "NULL";
    public static final String HEX_PREFIX = "0x";

    public static final String LEFT_QUOTATION_MARK = "'";
    public static final String RIGHT_QUOTATION_MARK = "'";
    public static final String VALUE_DELIMITER = ", ";

    // sql templates
    private static final String DROP_TABLE_TEMPLATE = "DROP TABLE IF EXISTS `%s`;";

    // multiple records separated into different insert statement
    private static final String SEPARATE_INSERT_SQL_TEMPLATE = "INSERT INTO `%s` VALUES (%s);";
    // multiple records compacted into one insert statement
    private static final String COMPACT_INSERT_SQL_PREFIX_TEMPLATE = "INSERT INTO `%s` VALUES ";
    // show create table statement
    private static final String SHOW_CREATE_TABLE_TEMPLATE = "SHOW CREATE TABLE `%s`";
    // used to retrieve records of table
    private static final String SELECT_DATA_TEMPLATE = "SELECT /*!40001 SQL_NO_CACHE */ * FROM `%s`";

    public TableDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump table.
     *
     * @param tableName name of the table
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void dump(String tableName) throws SQLException {
        String commentHead = String.format(COMMENT_STRUCTURE, tableName);
        SqlCommentUtility.printCommentHeader(printWriter, commentHead);
        String dropSql = String.format(DROP_TABLE_TEMPLATE, tableName);
        printWriter.println(dropSql);
        printWriter.println(getCreateTableSQL(tableName) + SQL_DELIMITER);

        printWriter.println();
        getCompactInsertSQL(tableName, printWriter);
        printWriter.println();

        printWriter.flush();
    }

    /**
     * Get SQL statement {@code String} that creates the specified table.
     *
     * @param tableName the name of the table
     * @return SQL statement {@code String}
     * @throws SQLException if a database access error occurs
     */
    private String getCreateTableSQL(String tableName) throws SQLException {
        String sql = String.format(SHOW_CREATE_TABLE_TEMPLATE, tableName);
        return SqlQueryUtility.queryString(connection, sql, 2);
    }

    /**
     * Get one insert SQL statement of data of table.
     *
     * @param tableName   the name of the table
     * @param printWriter where to print the generated SQL statement
     * @throws SQLException if a database access error occurs
     */
    public void getCompactInsertSQL(String tableName, PrintWriter printWriter) throws SQLException {
        String selectSQL = String.format(SELECT_DATA_TEMPLATE, tableName);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQL);


        boolean firstIteration = true;
        while (resultSet.next()) {
            if (firstIteration) {
                // print start of an insert statement
                String dataCommentHeader = String.format(COMMENT_RECORDS, tableName);
                SqlCommentUtility.printCommentHeader(printWriter, dataCommentHeader);
                String insertPrefix = String.format(COMPACT_INSERT_SQL_PREFIX_TEMPLATE, tableName);
                printWriter.print(insertPrefix);
                firstIteration = false;
            }
            String value = nextValue(resultSet);
            printWriter.print(value);

            if (!resultSet.isLast()) {
                printWriter.print(VALUE_DELIMITER);
            } else { // print delimiter after last record
                printWriter.println(SQL_DELIMITER);
            }
        }
        resultSet.close();
        statement.close();
    }

    /**
     * Get multiple insert SQL statements, each one statements corresponds
     * to one row of the table.
     *
     * @param tableName   the name of the table
     * @param printWriter where to print the generated SQL statement
     * @throws SQLException if a database access error occurs
     */
    public void getInsertSQL(String tableName, PrintWriter printWriter) throws SQLException {
        String selectSQL = String.format(SELECT_DATA_TEMPLATE, tableName);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQL);

        while (resultSet.next()) {
            String insertSQL = nextInsert(tableName, resultSet);
            printWriter.println(insertSQL);
        }

        resultSet.close();
        statement.close();
    }

    /**
     * Return a insert statement, corresponding to current row of resultSet.
     *
     * @param tableName the name of the table
     * @param resultSet contains the row to generate insert statement
     * @return insert statement
     * @throws SQLException if a database access error occurs
     */
    private String nextInsert(String tableName, ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        int columnCount = resultSet.getMetaData().getColumnCount();
        List<String> rowValues = new ArrayList<>(columnCount);
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            String columnClassName = resultSetMetaData.getColumnClassName(columnIndex);
            rowValues.add(getLiteralValue(resultSet, columnIndex, columnClassName));
        }
        String rowValue = String.join(VALUE_DELIMITER, rowValues);
        return String.format(SEPARATE_INSERT_SQL_TEMPLATE, tableName, rowValue);
    }


    /**
     * Returns a {@code String} used in value part of an insert statement.
     * The values in row is represented by corresponding literal string,
     * joined with comma and surrounded by brackets.
     *
     * @param resultSet where the values come from
     * @return value part of an insert statement representing the row.
     * @throws SQLException if a database access error occurs
     */
    private String nextValue(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<String> rowValues = new ArrayList<>(columnCount);
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            String columnClassName = resultSetMetaData.getColumnClassName(columnIndex);
            rowValues.add(getLiteralValue(resultSet, columnIndex, columnClassName));
        }
        String rowValue = String.join(VALUE_DELIMITER, rowValues);
        return String.format("(%s)", rowValue);
    }

    /**
     * Get literal value of a column's value.
     *
     * @param resultSet       where values come from
     * @param columnIndex     which column value comes from
     * @param columnClassName Returns the fully-qualified name of the Java class
     * @return Literal value of given column
     * @throws SQLException       if a database access error occurs
     */
    private String getLiteralValue(ResultSet resultSet, int columnIndex, String columnClassName) throws SQLException {
        switch (columnClassName) {
            case "java.lang.Boolean":
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.math.BigInteger":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.math.BigDecimal":
            case "java.lang.Short":
                return getColumnValue(resultSet, columnIndex, false, false);
            case "java.sql.Date":
            case "java.sql.Timestamp":
            case "java.sql.Time":
            case "java.time.LocalDateTime":
                return getColumnValue(resultSet, columnIndex, false, true);
            case "java.lang.String":
                return getColumnValue(resultSet, columnIndex, true, true);
            case "[B":
            case "java.lang.Object":
                return getHexColumnValue(resultSet, columnIndex);
            default:
                throw new IllegalStateException("Unexpected columnClassName: " + columnClassName);
        }
    }

    /**
     * Convert value from column specified by {@code columnIndex} in resultSet
     * to {@code String} for use in insert statement.
     *
     * @param resultSet   where values come from
     * @param columnIndex which column value comes from
     * @param escaping    whether escaping the value
     * @param quotation   whether surround the value with quotation mark
     * @return value of given column.
     * @throws SQLException if a database access error occurs
     */
    private String getColumnValue(ResultSet resultSet, int columnIndex,
                                  boolean escaping, boolean quotation) throws SQLException {
        String columnValue = resultSet.getString(columnIndex);
        if (resultSet.wasNull()) {
            columnValue = STRING_NULL;
        } else {
            if (escaping) {
                columnValue = SqlValueUtility.escapeString(columnValue);
            }
            if (quotation) {
                columnValue = LEFT_QUOTATION_MARK + columnValue + RIGHT_QUOTATION_MARK;
            }
        }
        return columnValue;
    }

    /**
     * Convert binary value from column specified by {@code columnIndex}
     * in resultSet to hex {@code String} for use in insert statement.
     *
     * @param resultSet   where values come from
     * @param columnIndex which column value comes from
     * @return hex value of given column.
     * @throws SQLException if a database access error occurs
     */
    private String getHexColumnValue(ResultSet resultSet, int columnIndex) throws SQLException {
        Blob blob = resultSet.getBlob(columnIndex);
        String columnValue = STRING_NULL;
        if (blob != null) {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            columnValue = HEX_PREFIX + SqlValueUtility.bytesToHex(bytes);
        }
        return columnValue;
    }

}
