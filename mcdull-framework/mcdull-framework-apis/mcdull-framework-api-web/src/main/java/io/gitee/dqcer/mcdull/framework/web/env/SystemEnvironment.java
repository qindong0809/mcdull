package io.gitee.dqcer.mcdull.framework.web.env;

/**
 * 系统环境信息，由 starter-web 装配后供业务代码注入。
 */
public class SystemEnvironment {

    /**
     * 是否生产环境
     */
    private Boolean isProd;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 环境编码
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
