package io.gitee.dqcer.mcdull.business.common.dump;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.simple.SimpleDataSource;
import io.gitee.dqcer.mcdull.business.common.mysql.AssembledFromDatabase;
import io.gitee.dqcer.mcdull.business.common.mysql.DiffSchema;
import io.gitee.dqcer.mcdull.business.common.mysql.Table;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class MysqlUtil {
    public static final String JDBC_FORMAT = "jdbc:mysql://{}:{}/{}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        String host = "mcdull.io";
//        Integer port = 3306;
//        String username = "root";
//        String password = "123456";
//        String databaseName = "";
//        boolean testConnect = testConnect(host, port, username, password, databaseName);



        Class.forName("com.mysql.cj.jdbc.Driver");
        String urlTmp = "jdbc:mysql://:3306/?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowMultiQueries=true&serverTimeZone=UTC&allowPublicKeyRetrieval=true";
        String url = "jdbc:mysql://3306/?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowMultiQueries=true&serverTimeZone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "123456";
        AssembledFromDatabase.Com source = AssembledFromDatabase.compare(urlTmp, user, password);
        AssembledFromDatabase.Com target = AssembledFromDatabase.compare(url, user, password);
        DiffSchema.Diff compare = DiffSchema.tableLevelCompare(source, target);
        StringBuffer buffer = new StringBuffer();
        for (Table table : compare.getAddList()) {
            buffer.append(DiffSchema.createTable(table));
        }

        for (Table table : compare.getNotExistList()) {
            buffer.append(DiffSchema.createTable(table));
        }

        System.out.println(buffer);

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

    public static Db getInstance(String jdbc, String username, String password) {
        DataSource ds = new SimpleDataSource(jdbc, username, password);
        return DbUtil.use(ds);
    }

    @SneakyThrows(Exception.class)
    public static String runScript(Db db, Reader reader){
        StringWriter logWriter = new StringWriter();
        Connection connection = db.getConnection();

        try {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            Resources.setCharset(StandardCharsets.UTF_8);
            scriptRunner.setEscapeProcessing(false);
            scriptRunner.setRemoveCRs(true);
            scriptRunner.setSendFullScript(false);
            scriptRunner.setAutoCommit(false);
            scriptRunner.setStopOnError(true);
            // 分隔符，还未验证具体功能
            scriptRunner.setFullLineDelimiter(false);
            // 每条命令间的分隔符，注意这个不建议用分号分隔
            // 因为SQL脚本中可以写存储过程，中间存在分号，导致存储过程执行失败
            scriptRunner.setDelimiter(";");
            scriptRunner.setLogWriter(new PrintWriter(logWriter));
            // 读取SQL文件路径获取SQL文件执行
            scriptRunner.runScript(reader);
            connection.commit();
            return logWriter.toString();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }finally {
            connection.close();
        }
    }

    public static String runSql(Db db, String sql){
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                sql.getBytes(StandardCharsets.UTF_8))));
        return runScript(db, reader);
    }

    public static String runSql(String host, Integer port, String username, String password, String databaseName, String sql){
        String jdbc = StrUtil.format(JDBC_FORMAT, host, port, databaseName);
        DataSource ds = new SimpleDataSource(jdbc, username, password);
        Db db = DbUtil.use(ds);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                sql.getBytes(StandardCharsets.UTF_8))));
        return runScript(db, reader);
    }
}
