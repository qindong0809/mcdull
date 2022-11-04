package com.dqcer.mcdull.auth.api.vo;

import com.dqcer.framework.base.VO;

/**
 * 登录VO
 *
 * @author dqcer
 * @date 2022/11/01
 */
public class LoginVO extends VO {

    private String username;

    private String email;

    private String tid;

    private String tenantName;

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
