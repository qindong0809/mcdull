package io.gitee.dqcer.mcdull.framework.base.storage;


/**
 * current user
 *
 * @author dqcer
 * @since 2024/09/29
 */
public interface IUserSession {

    String getUserId();

    void setUserId(String userId);

    String getLoginName();

    void setLoginName(String loginName);

    Integer getUserType();

    void setUserType(Integer userType);

    Integer getTenantId();

    void setTenantId(Integer tenantId);

    Boolean getAdministratorFlag();

    void setAdministratorFlag(Boolean administratorFlag);

    String getLanguage();

    void setLanguage(String language);

    /**
     * 时区标识符
     * e.g: Asia/Shanghai
     * {@link java.time.ZoneId#SHORT_IDS}
     */
    String getZoneIdStr();

    void setZoneIdStr(String zoneIdStr);

    String getDateFormat();

    void setDateFormat(String dateFormat);

    Boolean getAppendTimezoneStyle();

    void setAppendTimezoneStyle(Boolean appendTimezoneStyle);

}
