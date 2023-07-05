package io.gitee.dqcer.mcdull.business.common.mysql;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class DiffSchema {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String urlTmp = "jdbc:mysql://mcdull.io:3306/mcdull-tmp?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowMultiQueries=true&serverTimeZone=UTC&allowPublicKeyRetrieval=true";
        String url = "jdbc:mysql://mcdull.io:3306/mcdull-cloud?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowMultiQueries=true&serverTimeZone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "123456";
        AssembledFromDatabase.Com source = AssembledFromDatabase.compare(urlTmp, user, password);
        AssembledFromDatabase.Com target = AssembledFromDatabase.compare(url, user, password);
        Diff compare = compare(source, target);
        StringBuffer buffer = new StringBuffer();
        for (Table table : compare.getAdd()) {
            buffer.append(createTable(table));
        }

        for (Table table : compare.getNotExist()) {
            buffer.append(createTable(table));
        }

        System.out.println(buffer);

    }

    public static Diff compare(AssembledFromDatabase.Com source, AssembledFromDatabase.Com target) {
        Map<String, Table> targetMap = target.getTables().stream().collect(Collectors.toMap(Table::getTableName, Function.identity()));
        Map<String, Table> sourceMap = source.getTables().stream().collect(Collectors.toMap(Table::getTableName, Function.identity()));
        List<Table> addList = new ArrayList<>();
        List<Table> notExistList = new ArrayList<>();
        for (Map.Entry<String, Table> entry : targetMap.entrySet()) {
            if (!sourceMap.containsKey(entry.getKey())) {
                addList.add(entry.getValue());
            }
        }

        for (Map.Entry<String, Table> entry : sourceMap.entrySet()) {
            if (!targetMap.containsKey(entry.getKey())) {
                notExistList.add(entry.getValue());
            }
        }
        Diff diff = new Diff();
        diff.setAdd(addList);
        diff.setNotExist(notExistList);
        return diff;
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



    @Data
    public static class Diff {
        private List<Table> add;

        private List<Table> notExist;
    }
}

