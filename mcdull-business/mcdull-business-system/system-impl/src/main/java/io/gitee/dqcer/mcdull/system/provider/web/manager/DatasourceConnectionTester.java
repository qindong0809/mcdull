package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceTestVO;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

@Component
public class DatasourceConnectionTester {

    public DatasourceTestVO test(String jdbcUrl, String username, String password) {
        DatasourceTestVO vo = new DatasourceTestVO();
        long start = System.currentTimeMillis();
        try {
            DriverManager.setLoginTimeout(5);
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                DatabaseMetaData metaData = connection.getMetaData();
                vo.setSuccess(true);
                vo.setMessage("连接成功");
                vo.setDatabaseProduct(metaData.getDatabaseProductName());
                vo.setDatabaseVersion(metaData.getDatabaseProductVersion());
            }
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMessage(e.getClass().getSimpleName());
        }
        vo.setLatencyMs(System.currentTimeMillis() - start);
        return vo;
    }
}
