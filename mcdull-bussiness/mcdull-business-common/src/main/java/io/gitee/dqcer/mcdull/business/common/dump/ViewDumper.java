package io.gitee.dqcer.mcdull.business.common.dump;


import io.gitee.dqcer.mcdull.business.common.dump.util.SqlCommentUtility;
import io.gitee.dqcer.mcdull.business.common.dump.util.SqlQueryUtility;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is used to dump view.
 *
 * @author dqcer
 * @since 2024/11/18
 */
public class ViewDumper extends Dumper {

    private static final String SHOW_CREATE_VIEW_TEMPLATE = "SHOW CREATE VIEW `%s`";
    private static final String DROP_VIEW_TEMPLATE = "DROP VIEW IF EXISTS `%s`;";
    private static final String COMMENT_VIEW_STRUCTURE = "View structure for %s";


    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public ViewDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump view.
     *
     * @param viewName name of the view
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void dump(String viewName) throws SQLException {
        String structureHeadComment = String.format(COMMENT_VIEW_STRUCTURE, viewName);
        SqlCommentUtility.printCommentHeader(printWriter, structureHeadComment);
        String dropSql = String.format(DROP_VIEW_TEMPLATE, viewName);
        printWriter.println(dropSql);

        printWriter.println(getCreateViewSql(viewName) + SQL_DELIMITER);
        printWriter.println();

        printWriter.flush();
    }

    /**
     * Get SQL statement {@code String} that creates the specified view.
     *
     * @param viewName name of the view
     * @return SQL statement {@code String}
     * @throws SQLException if a database access error occurs
     */
    public String getCreateViewSql(String viewName) throws SQLException {
        String sql = String.format(SHOW_CREATE_VIEW_TEMPLATE, viewName);
        return SqlQueryUtility.queryString(this.connection, sql, 2);
    }
}
