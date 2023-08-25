package io.gitee.dqcer.mcdull.admin.util.dump.methods;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Dump function from database.
 *
 * @author Elyar Adil
 * @since 1.0
 */
public class FunctionDumper extends MethodDumper {
    // sql templates
    private static final String SHOW_CREATE_FUNCTION_TEMPLATE = "SHOW CREATE FUNCTION `%s`";
    private static final String DROP_FUNCTION_TEMPLATE = "DROP FUNCTION IF EXISTS `%s`;";
    // comment templates
    private static final String COMMENT_FUNCTION_STRUCTURE = "Function structure for %s";

    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public FunctionDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump function
     *
     * @param functionName name of the function
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void dump(String functionName) throws SQLException {
        dump(functionName, COMMENT_FUNCTION_STRUCTURE, DROP_FUNCTION_TEMPLATE, SHOW_CREATE_FUNCTION_TEMPLATE, 3);
    }

}
