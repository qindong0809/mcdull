package io.gitee.dqcer.mcdull.business.common.dump.methods;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Dump trigger from database.
 *
 * @author dqcer
 * @since 2024/11/18
 */
public class TriggerDumper extends MethodDumper {
    // sql templates
    private static final String SHOW_CREATE_TRIGGER_TEMPLATE = "SHOW CREATE TRIGGER `%s`";
    private static final String DROP_TRIGGER_TEMPLATE = "DROP TRIGGER IF EXISTS `%s`;";
    // comment templates
    private static final String COMMENT_TRIGGER_STRUCTURE = "Trigger structure for %s";

    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public TriggerDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump trigger
     *
     * @param triggerName name of the trigger
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void dump(String triggerName) throws SQLException {
        dump(triggerName, COMMENT_TRIGGER_STRUCTURE, DROP_TRIGGER_TEMPLATE, SHOW_CREATE_TRIGGER_TEMPLATE, 3);
    }


}
