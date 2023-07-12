package io.gitee.dqcer.mcdull.admin.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.simple.SimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MysqlUtil {
    public static final String JDBC_FORMAT = "jdbc:mysql://{}:{}/{}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";


    public static void main(String[] args) throws SQLException {
        String host = "mcdull.io";
        Integer port = 3306;
        String username = "root";
        String password = "123456";
        String databaseName = "";
        boolean testConnect = testConnect(host, port, username, password, databaseName);
    }

    public static boolean testConnect(String host, Integer port, String username, String password, String databaseName) {
        boolean success = true;
        Db db = getInstance(host, port, username, password, databaseName);
        Connection connection = null;
        try {
            connection = db.getConnection();
        } catch (Exception e) {
            success = false;
        } finally {
            if (connection != null) {
                db.closeConnection(connection);
            }
        }
        return success;
    }

    public static void close(Db db) {
        try (Connection connection = db.getConnection()) {
            if (connection != null) {
                db.closeConnection(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Db getInstance(String host, Integer port, String username, String password, String databaseName) {
        String jdbc = StrUtil.format(JDBC_FORMAT, host, port, databaseName);
        DataSource ds = new SimpleDataSource(jdbc, username, password);
        return DbUtil.use(ds);
    }
}
