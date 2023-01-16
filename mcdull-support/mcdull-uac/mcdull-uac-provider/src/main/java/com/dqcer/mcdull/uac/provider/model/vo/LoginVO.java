package com.dqcer.mcdull.uac.provider.model.vo;

import com.dqcer.mcdull.framework.base.vo.VO;

/**
 * 登录VO
 *
 * @author dqcer
 * @version 2022/11/01
 */
public class LoginVO implements VO {

    private String username;

    private String email;

    private String tid;

    private String tenantName;

    @Override
    public String toString() {
        return "LoginVO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", tid='" + tid + '\'' +
                ", tenantName='" + tenantName + '\'' +
                '}';
    }

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
