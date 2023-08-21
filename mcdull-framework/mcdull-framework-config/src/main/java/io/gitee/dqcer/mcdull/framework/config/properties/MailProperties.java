package io.gitee.dqcer.mcdull.framework.config.properties;


/**
 * mail properties
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class MailProperties {

    private String host;

    private String username;

    private String password;

    private Integer port;

    @Override
    public String toString() {
        return "MailProperties{" +
                "host='" + host + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
