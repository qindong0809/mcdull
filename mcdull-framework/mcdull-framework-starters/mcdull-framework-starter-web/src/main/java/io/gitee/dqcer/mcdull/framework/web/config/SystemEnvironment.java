package io.gitee.dqcer.mcdull.framework.web.config;


/**
 * 系统环境
 *
 */
public class SystemEnvironment {

    /**
     * 是否位生产环境
     */
    private Boolean isProd;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 环境
     */
    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Boolean getProd() {
        return isProd;
    }

    public void setProd(Boolean prod) {
        isProd = prod;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
