package io.gitee.dqcer.mcdull.framework.base.storage;


/**
 * current user
 *
 * @author dqcer
 * @since 2024/09/29
 */
public interface ICurrentUser {

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

}
