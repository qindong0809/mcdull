package com.dqcer.mcdull.admin.model.vo.sys;

import com.dqcer.mcdull.framework.base.annotation.Transform;
import com.dqcer.mcdull.framework.base.vo.VO;
import com.dqcer.mcdull.admin.config.OperationTypeEnum;
import com.dqcer.mcdull.admin.framework.transformer.UserTransformer;

/**
* 日志记录 返回客户端值
*
* @author dqcer
* @version 2023-01-14
*/
public class LogVO implements VO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 操作人的账号主键
     */
    private Long accountId;

    @Transform(from = "accountId", transformer = UserTransformer.class)
    private String accountStr;

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

    /**
     * 所属操作 str类型
     */
    @Transform(from = "type", dataSource = OperationTypeEnum.class)
    private String typeStr;

    public String getAccountStr() {
        return accountStr;
    }

    public LogVO setAccountStr(String accountStr) {
        this.accountStr = accountStr;
        return this;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public LogVO setTypeStr(String typeStr) {
        this.typeStr = typeStr;
        return this;
    }

    public String getModel() {
        return model;
    }

    public LogVO setModel(String model) {
        this.model = model;
        return this;
    }

    public String getMenu() {
        return menu;
    }

    public LogVO setMenu(String menu) {
        this.menu = menu;
        return this;
    }

    public String getType() {
        return type;
    }

    public LogVO setType(String type) {
        this.type = type;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
            "id=" + id +
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