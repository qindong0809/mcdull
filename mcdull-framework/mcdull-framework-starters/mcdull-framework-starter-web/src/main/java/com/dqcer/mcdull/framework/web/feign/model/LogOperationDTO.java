package io.gitee.dqcer.framework.web.feign.model;

import io.gitee.dqcer.framework.base.dto.DTO;

import java.util.Date;

/**
 * 日志 dto
 *
 * @author dqcer
 * @version 2022/12/26
 */
public class LogOperationDTO implements DTO {

    private String userAgent;


    /**
     * 操作人的账号主键
     */
    private Long accountId;

    /**
     * 租户主键
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 路径
     */
    private String path;

    /**
     * 耗时
     */
    private Long timeTaken;

    /**
     * 参数map
     */
    private String parameterMap;

    /**
     * 请求头
     */
    private String headers;

    /**
     * 跟踪id
     */
    private String traceId;

    @Override
    public String toString() {
        return "LogDTO{" +
                "userAgent='" + userAgent + '\'' +
                ", accountId=" + accountId +
                ", tenantId=" + tenantId +
                ", createdTime=" + createdTime +
                ", clientIp='" + clientIp + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", timeTaken=" + timeTaken +
                ", parameterMap='" + parameterMap + '\'' +
                ", headers='" + headers + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(String parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getTraceId() {
        return traceId;
    }

    public LogOperationDTO setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
