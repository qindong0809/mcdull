package io.gitee.dqcer.mcdull.business.common.mysql;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class DiffSchema {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://mcdull.io:3306/mcdull-cloud?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowMultiQueries=true&serverTimeZone=UTC";
        String user = "root";
        String password = "123456";
        AssembledFromDatabase source = new AssembledFromDatabase(url, user, password);
        AssembledFromDatabase target = new AssembledFromDatabase(url, user, password);
        System.out.println(compare(source, target));


    }

    public static boolean compare(AssembledFromDatabase source, AssembledFromDatabase target) {
        Map<String, Table> targetMap = target.getTables().stream().collect(Collectors.toMap(Table::getTableName, Function.identity()));
        Map<String, Table> sourceMap = source.getTables().stream().collect(Collectors.toMap(Table::getTableName, Function.identity()));
        for (Map.Entry<String, Table> entry : targetMap.entrySet()) {
            if (!sourceMap.containsKey(entry.getKey())) {
                return false;
            }
        }

        for (Map.Entry<String, Table> entry : sourceMap.entrySet()) {
            if (targetMap.containsKey(entry.getKey())) {
                return false;
            }
        }
        return true;
    }


    private static StringBuffer createTable(Table table) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("create table ").append(table.getTableName()).append("(\n");
        Boolean primary = false;
        String primaryKey = "";
        List<Column> columnList = table.columnList;
        for (Column column : columnList) {
            buffer.append(column.getName())
                    .append(StrUtil.SPACE)
                    .append(column.getType().toLowerCase())
                    .append(String.format("(%s)", column.getLength()))
                    .append(StrUtil.SPACE)
                    .append(column.getNotNull() ? "not null" : "default null")
                    .append(StrUtil.isNotBlank(column.getComment()) ? StrUtil.SPACE + "comment " + "'" + column.getComment() + "'" : "")
            ;
            buffer.append(",\n");
            if (primary) {
                continue;
            }
            primary = column.getPrimary();
            if (primary) {
                primaryKey = column.getName();
            }
        }
        if (primary) {
            buffer.append(String.format("primary key (%s)", primaryKey));
            buffer.append(",\n");
        }

        buffer.deleteCharAt(buffer.length() - 2);

        buffer.append(") engine=innodb");
        String comment = table.getTableComment();
        if (StrUtil.isNotBlank(comment)) {
            buffer.append(" comment = ").append(comment);
        }
        buffer.append(";\n");
        return buffer;
    }
}
