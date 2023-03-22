package io.gitee.dqcer.mcdull.framework.mysql.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 动态数据源属性
 *
 * @author dqcer
 * @since 2021/08/31
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    public static final String DRUID = "Druid";

    public static final String HIKARI = "Hikari";


    /**
     * 默认的key
     */
    private String defaultName = "master";

    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url 地址
     */
    private String url;
    /**
     * JDBC 用户名
     */
    private String username;
    /**
     * JDBC 密码
     */
    private String password;

    /**
     * 连接池类型 默认 Hikari / Druid
     */
    private String poolType = HIKARI;

    private String druidUsername;

    private String druidPassword;

    /**
     * 租户sql
     */
    private String tenantSql;

    public String getDruidUsername() {
        return druidUsername;
    }

    public void setDruidUsername(String druidUsername) {
        this.druidUsername = druidUsername;
    }

    public String getDruidPassword() {
        return druidPassword;
    }

    public void setDruidPassword(String druidPassword) {
        this.druidPassword = druidPassword;
    }

    public String getPoolType() {
        return poolType;
    }

    public DataSourceProperties setPoolType(String poolType) {
        this.poolType = poolType;
        return this;
    }

    public String getTenantSql() {
        return tenantSql;
    }

    public void setTenantSql(String tenantSql) {
        this.tenantSql = tenantSql;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
