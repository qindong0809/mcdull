package io.gitee.dqcer.mcdull.mdc.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;

import java.util.Date;

/**
 * 日志 实体
 *
 * @author dqcer
 * @since 2022/12/26
 */
@TableName("sys_log")
public class LogEntity extends IdEntity {

    /**
     * 操作人的账号主键
     */
    private Integer accountId;

    /**
     * 租户主键
     */
    private Integer tenantId;

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

    @Override
    public String toString() {
        return "LogDO{" +
                "accountId=" + accountId +
                ", tenantId=" + tenantId +
                ", createdTime=" + createdTime +
                ", clientIp='" + clientIp + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", traceId='" + traceId + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", timeTaken=" + timeTaken +
                ", parameterMap='" + parameterMap + '\'' +
                ", headers='" + headers + '\'' +
                ", id=" + id +
                "} " + super.toString();
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
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

    public LogEntity setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
