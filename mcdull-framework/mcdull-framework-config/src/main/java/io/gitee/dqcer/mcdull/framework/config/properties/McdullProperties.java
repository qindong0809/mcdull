package io.gitee.dqcer.mcdull.framework.config.properties;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mcdull 全局配置
 *
 * @author dqcer
 * @since 2022/12/26
 */
@ConfigurationProperties(prefix = GlobalConstant.ROOT_PREFIX)
public class McdullProperties {

    /**
     * 网关
     */
    private GatewayProperties gateway = new GatewayProperties();

    /**
     * 线程池
     */
    private ThreadPoolProperties threadPool = new ThreadPoolProperties();

    /**
     * oss对象存储
     */
    private OssProperties oss = new OssProperties();

    private MailProperties mail = new MailProperties();

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public MailProperties getMail() {
        return mail;
    }

    public void setMail(MailProperties mail) {
        this.mail = mail;
    }

    public OssProperties getOss() {
        return oss;
    }

    public void setOss(OssProperties oss) {
        this.oss = oss;
    }

    public ThreadPoolProperties getThreadPool() {
        return threadPool;
    }

    public McdullProperties setThreadPool(ThreadPoolProperties threadPool) {
        this.threadPool = threadPool;
        return this;
    }

    public GatewayProperties getGateway() {
        return gateway;
    }

    public void setGateway(GatewayProperties gateway) {
        this.gateway = gateway;
    }
}
