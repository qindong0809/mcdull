package io.gitee.dqcer.mcdull.business.common.dump;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Db;
import io.gitee.dqcer.mcdull.business.common.dump.methods.EventDumper;
import io.gitee.dqcer.mcdull.business.common.dump.methods.FunctionDumper;
import io.gitee.dqcer.mcdull.business.common.dump.methods.ProcedureDumper;
import io.gitee.dqcer.mcdull.business.common.dump.methods.TriggerDumper;
import io.gitee.dqcer.mcdull.business.common.dump.util.DumpInfoUtility;
import io.gitee.dqcer.mcdull.business.common.dump.util.SqlQueryUtility;
import io.gitee.dqcer.mcdull.business.common.dump.util.SqlShowUtility;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SqlDumper {

    public static void main(String[] args) throws SQLException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        List<String> list = new ArrayList<>();
        list.add("mcdull-cloud-3");

        // 9e44c435d2498708c1781903c0bc753d
        Db db = MysqlUtil.getInstance("mcdull.io", 3306, "root", "123456", "mcdull-cloud-3");
        File file = dumpDatabase(db.getConnection(), new HashSet<>(list), "D://ddd-1.sql");
        outputStream.close();
        System.out.println("dddd");
//        System.out.println(outputStream.toString());
    }





    @SneakyThrows(Exception.class)
    public static File dumpDatabase(Connection connection, Set<String>  databaseSet, String dumpNamePath) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        DumpInfoUtility.printHeadInfo(connection, printWriter);
        DumpInfoUtility.printDumpPrefix(printWriter);

        List<String> databaseList = SqlShowUtility.listDatabase(connection);
        for (String databaseName : databaseList) {
            if (CollUtil.isNotEmpty(databaseSet)) {
                if (databaseSet.contains(databaseName)) {
                    dumpDatabase(databaseName, printWriter, connection);
                }
            }
        }
        DumpInfoUtility.printDumpSuffix(printWriter);
        printWriter.flush();
        printWriter.close();

        return FileUtil.writeBytes(outputStream.toByteArray(), dumpNamePath);
    }


    private static void dumpDatabase(String databaseName, PrintWriter printWriter, Connection connection) throws SQLException {
        SqlQueryUtility.selectDatabase(connection, databaseName);
        DatabaseDumper databaseDumper = new DatabaseDumper(connection, printWriter);
        TableDumper tableDumper = new TableDumper(connection, printWriter);
        TriggerDumper triggerDumper = new TriggerDumper(connection, printWriter);
        ViewDumper viewDumper = new ViewDumper(connection, printWriter);
        FunctionDumper functionDumper = new FunctionDumper(connection, printWriter);
        ProcedureDumper procedureDumper = new ProcedureDumper(connection, printWriter);
        EventDumper eventDumper = new EventDumper(connection, printWriter);

        databaseDumper.dump(databaseName);

        printWriter.println(String.format("USE `%s`;", databaseName));

        List<String> tableList = SqlShowUtility.listTable(connection, databaseName);
        for (String table : tableList) {
            if (table.equals("sys_area")) {
                continue;
            }
            tableDumper.dump(table);
            List<String> triggerList = SqlShowUtility.listTriggerOfTable(connection, databaseName, table);
            for (String trigger : triggerList) {
                triggerDumper.dump(trigger);
            }
        }

        List<String> viewList = SqlShowUtility.listView(connection, databaseName);
        for (String view : viewList) {
            viewDumper.dump(view);
        }

        List<String> functionList = SqlShowUtility.listFunction(connection, databaseName);
        for (String function : functionList) {
            functionDumper.dump(function);
        }

        List<String> procedureList = SqlShowUtility.listProcedure(connection, databaseName);
        for (String procedure : procedureList) {
            procedureDumper.dump(procedure);
        }

        List<String> eventList = SqlShowUtility.listEvent(connection, databaseName);
        for (String event : eventList) {
            eventDumper.dump(event);
        }
    }

}
