package io.gitee.dqcer.mcdull.framework.base.storage;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一会话
 *
 * @author dqcer
 * @since 2021/11/13
 */
public class UnifySession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号主键
     */
    private Integer userId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 所属客户
     */
    private Integer tenantId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 语言
     */
    private String language;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 当前时间
     */
    private Date now;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 扩展
     */
    private ConcurrentHashMap<String, Object> extension;

    @Override
    public String toString() {
        return "UnifySession{" +
                "userId=" + userId +
                ", tenantId=" + tenantId +
                ", roleId=" + roleId +
                ", language='" + language + '\'' +
                ", traceId='" + traceId + '\'' +
                ", extension=" + extension +
                '}';
    }

    public Integer getUserType() {
        return userType;
    }

    public UnifySession setUserType(Integer userType) {
        this.userType = userType;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public ConcurrentHashMap<String, Object> getExtension() {
        return extension == null ? new ConcurrentHashMap<>(8) : extension;
    }

    public void setExtension(ConcurrentHashMap<String, Object> extension) {
        this.extension = extension;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
