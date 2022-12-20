package com.dqcer.mcdull.mdc.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dqcer.framework.base.entity.IdDO;

import java.util.Date;

@TableName("sys_log")
public class LogDO extends IdDO {

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
     * 用户代理
     */
    private String userAgent;

    /**
     * 跟踪id
     */
    private String traceId;

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

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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

    public LogDO setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
