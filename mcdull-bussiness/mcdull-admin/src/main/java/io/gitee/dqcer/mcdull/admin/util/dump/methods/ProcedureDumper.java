package io.gitee.dqcer.mcdull.admin.util.dump.methods;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Dump procedure from database.
 *
 * @author Elyar Adil
 * @since 1.0
 */
public class ProcedureDumper extends MethodDumper {
    // sql templates
    private static final String SHOW_CREATE_PROCEDURE_TEMPLATE = "SHOW CREATE PROCEDURE `%s`";
    private static final String DROP_PROCEDURE_TEMPLATE = "DROP PROCEDURE IF EXISTS `%s`;";
    // comment templates
    private static final String COMMENT_PROCEDURE_STRUCTURE = "Procedure structure for %s";

    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public ProcedureDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump procedure
     *
     * @param procedureName name of the procedure
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void dump(String procedureName) throws SQLException {
        dump(procedureName, COMMENT_PROCEDURE_STRUCTURE, DROP_PROCEDURE_TEMPLATE, SHOW_CREATE_PROCEDURE_TEMPLATE, 3);
    }

}
