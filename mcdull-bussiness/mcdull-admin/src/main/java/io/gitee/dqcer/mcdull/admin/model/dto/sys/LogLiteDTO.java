package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 日志记录 接收客户端参数
*
* @author dqcer
* @version 2023-01-14
*/
public class LogLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.One.class})
     private Long id;

    /**
     * 操作人的账号主键
     */
    @NotNull(groups = {ValidGroup.Add.class})
    private Long accountId;

    /**
     * 租户主键
     */
    @NotNull(groups = {ValidGroup.Add.class})
    private Long tenantId;

    /**
     * 客户端ip
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String clientIp;

    /**
     * 用户代理
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String userAgent;

    /**
     * 请求方法
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String method;

    /**
     * 路径
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String path;

    /**
     * 日志跟踪id
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String traceId;

    /**
     * 耗时
     */
    @NotNull(groups = {ValidGroup.Add.class})
    private Long timeTaken;

    /**
     * 参数map
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String parameterMap;

    /**
     * 请求头
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String headers;


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