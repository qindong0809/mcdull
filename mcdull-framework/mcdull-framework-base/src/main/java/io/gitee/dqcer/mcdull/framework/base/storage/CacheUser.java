package io.gitee.dqcer.mcdull.framework.base.storage;

import java.io.Serializable;

/**
 * 缓存的用户
 *
 * @author dqcer
 * @since 2021/11/14
 */
public class CacheUser implements ICurrentUser, Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String loginName;

    private Integer tenantId;

    private Boolean administratorFlag;

    private String language;

    private String zoneIdStr;

    private String dateFormat;

    private Boolean appendTimezoneStyle;

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Boolean getAppendTimezoneStyle() {
        return appendTimezoneStyle;
    }

    @Override
    public void setAppendTimezoneStyle(Boolean appendTimezoneStyle) {
        this.appendTimezoneStyle = appendTimezoneStyle;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public Integer getUserType() {
        return 0;
    }

    @Override
    public void setUserType(Integer userType) {

    }

    @Override
    public Integer getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public Boolean getAdministratorFlag() {
        return administratorFlag;
    }

    @Override
    public void setAdministratorFlag(Boolean administratorFlag) {
        this.administratorFlag = administratorFlag;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getZoneIdStr() {
        return zoneIdStr;
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
}
