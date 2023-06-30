package io.gitee.dqcer.mcdull.business.common.mysql;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AssembledFromDatabase implements AssembledDatabaseMetadata {

    private String driver = "com.mysql.cj.jdbc.Driver";

    @SneakyThrows
    public AssembledFromDatabase(String url, String user, String pwd) {
        Class.forName(driver);
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pwd);
        props.setProperty("remarks", "true");
        props.setProperty("useInformationSchema", "true");
        Connection connection = DriverManager.getConnection(url, props);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tableRs = metaData.getTables(connection.getCatalog(), connection.getCatalog(), "%", new String[]{"TABLE"});
        while (tableRs.next()) {

            String tableName = tableRs.getString("TABLE_NAME");

            List<Column> columns = new ArrayList<>();
            Table table = new Table(tableName, tableRs.getString("REMARKS"), columns);
            String primaryColumn = null;
            ResultSet idxRs = metaData.getIndexInfo(connection.getCatalog(), connection.getCatalog(), tableName, false, false);
            while (idxRs.next()) {
                String indexName = idxRs.getString("INDEX_NAME");
                if ("PRIMARY".equals(indexName)) {
                    primaryColumn = idxRs.getString("COLUMN_NAME");
                    continue;
                }
                List<Index> exist = indices.stream().filter(o -> o.getTableName().equals(tableName) && o.getIndexName().equals(indexName)).collect(Collectors.toList());
                Index index;
                if (exist != null && exist.size() > 0) {
                    index = exist.get(0);
                    index.setParams(index.getParams() + "," + idxRs.getString("COLUMN_NAME"));
                } else {
                    index = new Index();
                    index.setTableName(tableName);
                    index.setIndexName(indexName);
                    index.setUnique(!idxRs.getBoolean("NON_UNIQUE"));
                    index.setParams(idxRs.getString("COLUMN_NAME"));
                    indices.add(index);
                }
            }
            ResultSet columnRs = metaData.getColumns(connection.getCatalog(), connection.getCatalog(), tableName, "%");
            while (columnRs.next()) {
                Column column = new Column();
                column.setName(columnRs.getString("COLUMN_NAME"));
                column.setType(columnRs.getString("TYPE_NAME"));
                column.setLength(columnRs.getString("COLUMN_SIZE"));
                column.setDecimalDigits(columnRs.getString("DECIMAL_DIGITS"));
                column.setNotNull(!columnRs.getBoolean("NULLABLE"));
                column.setPrimary(column.getName().equals(primaryColumn));
                column.setComment(columnRs.getString("REMARKS"));
                columns.add(column);
            }
            tables.add(table);
        }
    }
}
