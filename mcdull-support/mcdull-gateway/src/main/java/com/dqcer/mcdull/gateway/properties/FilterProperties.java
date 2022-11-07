package com.dqcer.mcdull.gateway.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤配置
 *
 * @author dqcer
 * @date 2022/10/27
 */
public class FilterProperties {

    /**
     * 不需要身份验证
     */
    private List<String> noAuth = new ArrayList<>();

    /**
     * 启用多租户模式
     */
    private Boolean multiTenant = false;

    public Boolean getMultiTenant() {
        return multiTenant;
    }

    public void setMultiTenant(Boolean multiTenant) {
        this.multiTenant = multiTenant;
    }

    public List<String> getNoAuth() {
        return noAuth;
    }

    public void setNoAuth(List<String> noAuth) {
        this.noAuth = noAuth;
    }

}
