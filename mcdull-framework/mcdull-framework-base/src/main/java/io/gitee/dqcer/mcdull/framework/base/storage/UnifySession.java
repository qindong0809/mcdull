package io.gitee.dqcer.mcdull.framework.base.storage;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一会话
 *
 * @author dqcer
 * @since 2021/11/13
 */
public class UnifySession<T> implements ICurrentUser, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号主键
     */
    private T userId;

    /**
     * 用户类型
     */
    private Integer userType;

    private Boolean administratorFlag;

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
     * 时区标识符
     *
     * e.g: Asia/Shanghai
     * {@link java.time.ZoneId#SHORT_IDS}
     */
    private String zoneIdStr;

    /**
     * 日期格式
     */
    private String dateFormat;

    private Locale locale;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 权限代码
     */
    private String permissionCode;

    private String loginName;

    /**
     * 扩展
     */
    private ConcurrentHashMap<String, Object> extension;

    @Override
    public String toString() {
        return new StringJoiner(", ", UnifySession.class.getSimpleName() + "[", "]")
                .add("userId=" + userId)
                .add("userType=" + userType)
                .add("administratorFlag=" + administratorFlag)
                .add("tenantId=" + tenantId)
                .add("roleId=" + roleId)
                .add("language='" + language + "'")
                .add("traceId='" + traceId + "'")
                .add("now=" + now)
                .add("zoneIdStr='" + zoneIdStr + "'")
                .add("dateFormat='" + dateFormat + "'")
                .add("requestUrl='" + requestUrl + "'")
                .add("extension=" + extension)
                .add("permissionCode=" + permissionCode)
                .toString();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Locale getLocale() {
        return locale;
    }

    public UnifySession<T> setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    @Override
    public Integer getTenantId() {
        return this.tenantId;
    }

    @Override
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public Boolean getAdministratorFlag() {
        return this.administratorFlag;
    }

    @Override
    public void setAdministratorFlag(Boolean administratorFlag) {
        this.administratorFlag = administratorFlag;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getZoneIdStr() {
        return this.zoneIdStr;
    }

    @Override
    public void setZoneIdStr(String zoneIdStr) {
        this.zoneIdStr = zoneIdStr;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Integer getUserType() {
        return userType;
    }

    public UnifySession<T> setUserType(Integer userType) {
        this.userType = userType;
        return this;
    }

    public T getUserId() {
        return userId;
    }

    public void setUserId(T userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
