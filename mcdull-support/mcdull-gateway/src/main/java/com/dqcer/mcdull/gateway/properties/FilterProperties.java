package io.gitee.dqcer.gateway.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤配置
 *
 * @author dqcer
 * @version 2022/10/27
 */
public class FilterProperties {

    /**
     * 不需要身份验证
     */
    private List<String> noAuth = new ArrayList<>();

    /**
     * 是否启用多租户模式
     */
    private Boolean enableMultiTenant = false;

    /**
     * 是否启用traceId
     */
    private Boolean enableTrace = false;

    public Boolean getEnableTrace() {
        return enableTrace;
    }

    public void setEnableTrace(Boolean enableTrace) {
        this.enableTrace = enableTrace;
    }

    public Boolean getEnableMultiTenant() {
        return enableMultiTenant;
    }

    public void setEnableMultiTenant(Boolean enableMultiTenant) {
        this.enableMultiTenant = enableMultiTenant;
    }

    public List<String> getNoAuth() {
        return noAuth;
    }

    public void setNoAuth(List<String> noAuth) {
        this.noAuth = noAuth;
    }

}
