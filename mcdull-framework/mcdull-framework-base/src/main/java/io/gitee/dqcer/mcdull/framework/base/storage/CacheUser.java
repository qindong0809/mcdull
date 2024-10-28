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

    private Integer tenantId;

    private Boolean administratorFlag;

    private String language;

    private String zoneIdStr;

    private String dateFormat;

    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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
