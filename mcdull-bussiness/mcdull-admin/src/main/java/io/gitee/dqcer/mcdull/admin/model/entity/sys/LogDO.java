package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;

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
     * 所属系统
     */
    private String model;

    /**
     * 所属菜单
     */
    private String menu;

    /**
     * 所属操作类型
     */
    private String type;

    public String getModel() {
        return model;
    }

    public LogDO setModel(String model) {
        this.model = model;
        return this;
    }

    public String getMenu() {
        return menu;
    }

    public LogDO setMenu(String menu) {
        this.menu = menu;
        return this;
    }

    public String getType() {
        return type;
    }

    public LogDO setType(String type) {
        this.type = type;
        return this;
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
    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    @Override
    public String toString() {
        return "SysLog{" +
            ", accountId=" + accountId +
            ", tenantId=" + tenantId +
            ", clientIp=" + clientIp +
            ", userAgent=" + userAgent +
            ", method=" + method +
            ", path=" + path +
            ", traceId=" + traceId +
            ", timeTaken=" + timeTaken +
            ", parameterMap=" + parameterMap +
            ", headers=" + headers +
        "}";
    }
}
