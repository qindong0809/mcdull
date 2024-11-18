package io.gitee.dqcer.mcdull.business.common.dump;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract dumper class.
 *
 * @author dqcer
 * @since 2024/11/18
 */
public abstract class Dumper {

    public static final String SQL_DELIMITER = ";";

    protected Connection connection;

    protected PrintWriter printWriter;

    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public Dumper(Connection connection, PrintWriter printWriter) {
        this.connection = connection;
        this.printWriter = printWriter;
    }

    /**
     * Abstract dump method
     * @param targetName name of the target
     * @throws SQLException if a database access error occurs
     */
    public abstract void dump(String targetName) throws SQLException;

}
