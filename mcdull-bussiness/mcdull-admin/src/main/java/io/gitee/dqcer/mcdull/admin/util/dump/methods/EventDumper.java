package io.gitee.dqcer.mcdull.admin.util.dump.methods;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Dump event from database.
 *
 * @author Elyar Adil
 * @since 1.0
 */
public class EventDumper extends MethodDumper {
    // sql templates
    private static final String SHOW_CREATE_EVENT_TEMPLATE = "SHOW CREATE EVENT `%s`";
    private static final String DROP_EVENT_TEMPLATE = "DROP EVENT IF EXISTS `%s`;";
    // comment templates
    private static final String COMMENT_EVENT_STRUCTURE = "Event structure for %s";


    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public EventDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump event
     *
     * @param eventName name of the event
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void dump(String eventName) throws SQLException {
        dump(eventName, COMMENT_EVENT_STRUCTURE, DROP_EVENT_TEMPLATE, SHOW_CREATE_EVENT_TEMPLATE, 4);
    }

}
