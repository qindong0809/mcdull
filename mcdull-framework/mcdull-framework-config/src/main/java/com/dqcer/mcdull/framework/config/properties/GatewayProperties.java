package com.dqcer.mcdull.framework.config.properties;

import java.util.ArrayList;
import java.util.List;

public class GatewayProperties {

    /**
     * 不需要身份验证
     */
    private List<String> noAuth = new ArrayList<>();

    public List<String> getNoAuth() {
        return noAuth;
    }

    public void setNoAuth(List<String> noAuth) {
        this.noAuth = noAuth;
    }
}
