package io.gitee.dqcer.mcdull.admin.util.dump.util;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Wrap "SHOW ..." query. Used to list entities like databases,
 * tables, views...
 */
public class SqlShowUtility {
    // show statement templates
    private final static String SHOW_TABLES = "SHOW FULL TABLES WHERE Table_type != 'VIEW'"; // name at column 1
    private final static String SHOW_VIEWS = "SHOW FULL TABLES WHERE Table_type = 'VIEW'"; // name at column 1
    private final static String SHOW_DATABASES = "SHOW DATABASES"; // name at column 1
    private final static String SHOW_FUNCTIONS = "SHOW FUNCTION STATUS WHERE Db = '%s'"; // name at column 2
    private final static String SHOW_TRIGGERS = "SHOW TRIGGERS"; // name at column 1
    private final static String SHOW_EVENTS = "SHOW EVENTS WHERE Db = '%s'"; // name at column 2
    private final static String SHOW_PROCEDURES = "SHOW PROCEDURE STATUS WHERE Db LIKE '%s'"; // name at column 2
    private final static String SHOW_TRIGGERS_OF_TABLE = "SHOW TRIGGERS WHERE `Table` LIKE '%s'"; // name at column 1

    /**
     * List databases
     *
     * @param connection sql connection
     * @return list of databases
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listDatabase(Connection connection) throws SQLException {
        return SqlQueryUtility.queryStringList(connection, SHOW_DATABASES, 1);
    }

    /**
     * List tables
     *
     * @param connection sql connection
     * @return list of tables
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listTable(Connection connection, String database) throws SQLException {
        SqlQueryUtility.selectDatabase(connection, database);
        return SqlQueryUtility.queryStringList(connection, SHOW_TABLES, 1);
    }

    /**
     * List views
     *
     * @param connection sql connection
     * @return list of views
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listView(Connection connection, String database) throws SQLException {
//        SqlQueryUtility.selectDatabase(connection, database);
        return SqlQueryUtility.queryStringList(connection, SHOW_VIEWS, 1);
    }

    /**
     * List functions
     *
     * @param connection sql connection
     * @return list of functions
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listFunction(Connection connection, String database) throws SQLException {
        return SqlQueryUtility.queryStringList(connection, String.format(SHOW_FUNCTIONS, database), 2);
    }

    /**
     * List procedures
     *
     * @param connection sql connection
     * @return list of procedures
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listProcedure(Connection connection, String database) throws SQLException {
        return SqlQueryUtility.queryStringList(connection, String.format(SHOW_PROCEDURES, database), 2);
    }

    /**
     * List triggers
     *
     * @param connection sql connection
     * @return list of triggers
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listTrigger(Connection connection, String database) throws SQLException {
        SqlQueryUtility.selectDatabase(connection, database);
        return SqlQueryUtility.queryStringList(connection, SHOW_TRIGGERS, 1);
    }

    /**
     * List triggers of given table.
     *
     * @param connection sql connection
     * @param tableName  name of the table
     * @return list of triggers
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listTriggerOfTable(Connection connection, String database, String tableName) throws SQLException {
//        SqlQueryUtility.selectDatabase(connection, database);
        return SqlQueryUtility.queryStringList(connection, String.format(SHOW_TRIGGERS_OF_TABLE, tableName), 1);
    }

    /**
     * List events
     *
     * @param connection sql connection
     * @return list of events
     * @throws SQLException if a database access error occurs
     */
    public static List<String> listEvent(Connection connection, String database) throws SQLException {
        return SqlQueryUtility.queryStringList(connection, String.format(SHOW_EVENTS, database), 2);
    }
}
