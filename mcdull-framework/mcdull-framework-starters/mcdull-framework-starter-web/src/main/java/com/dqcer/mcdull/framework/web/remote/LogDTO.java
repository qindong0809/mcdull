package com.dqcer.mcdull.framework.web.remote;

import java.util.Date;

public class LogDTO {

    /**
     * 是否为移动端
     */
    private int mobile;

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
     * 客户端
     */
    private String browser;

    /**
     * 平台
     */
    private String platform;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 引擎
     */
    private String engine;

    /**
     * 版本
     */
    private String version;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 操作时间
     */
    private Date time;

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
     * http状态
     */
    private Long status;

    /**
     * 参数map
     */
    private String parameterMap;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 请求头
     */
    private String headers;

    /**
     * 响应头
     */
    private String responseBody;

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
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

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(String parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }}
