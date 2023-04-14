package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;

import java.util.StringJoiner;

/**
 * 用户操作日志 实体类
 *
 * @author dqcer
 * @since 2023-01-14
 */
@TableName("sys_operation_log")
public class LogDO extends MiddleDO {

    private static final long serialVersionUID = 1L;

   /**
    * 操作人的账号主键
    */
    private Long accountId;

   /**
    * 租户主键
    */
    private Long tenantId;

   /**
    * 客户端ip
    */
    private String clientIp;

   /**
    * 用户代理
    */
    private String userAgent;

   /**
    * 请求方法
    */
    private String method;

   /**
    * 路径
    */
    private String path;

   /**
    * 日志跟踪id
    */
    private String traceId;

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
     * 所属操作按钮
     */
    private String button;

    @Override
    public String toString() {
        return new StringJoiner(", ", LogDO.class.getSimpleName() + "[", "]")
                .add("accountId=" + accountId)
                .add("tenantId=" + tenantId)
                .add("clientIp='" + clientIp + "'")
                .add("userAgent='" + userAgent + "'")
                .add("method='" + method + "'")
                .add("path='" + path + "'")
                .add("traceId='" + traceId + "'")
                .add("timeTaken=" + timeTaken)
                .add("parameterMap='" + parameterMap + "'")
                .add("headers='" + headers + "'")
                .add("button='" + button + "'")
                .add("createdTime=" + createdTime)
                .add("id=" + id)
                .toString();
    }

    public Long getAccountId() {
        return accountId;
    }

    public LogDO setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public LogDO setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public LogDO setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public LogDO setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public LogDO setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getPath() {
        return path;
    }

    public LogDO setPath(String path) {
        this.path = path;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public LogDO setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public LogDO setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
        return this;
    }

    public String getParameterMap() {
        return parameterMap;
    }

    public LogDO setParameterMap(String parameterMap) {
        this.parameterMap = parameterMap;
        return this;
    }

    public String getHeaders() {
        return headers;
    }

    public LogDO setHeaders(String headers) {
        this.headers = headers;
        return this;
    }

    public String getButton() {
        return button;
    }

    public LogDO setButton(String button) {
        this.button = button;
        return this;
    }
}
