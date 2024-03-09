package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.gitee.dqcer.mcdull.framework.base.vo.VO;

import java.util.Date;
import java.util.List;

/**
 * 登录视图对象
 *
 * @author dqcer
 * @since 2024/03/09
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LogonVO implements VO {

    /**
     *  accessToken: string;
     *   /** `accessToken`的过期时间（时间戳）
     * expires:T;
     *   /** 用于调用刷新accessToken的接口时所需的token
      *       *refreshToken:string;
     *   /** 用户名
      *       *username?:string;
     *   /** 当前登陆用户的角色
     *        *roles?:Array<string>;
     */

    private String accessToken;
    private Date expires;
    private String refreshToken;
    private String username;
    private List<String> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
