package io.gitee.dqcer.framework.base.storage;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 缓存的用户
 *
 * @author dqcer
 * @version 2021/11/14
 */
public class CacheUser implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 在线
     */
    public static final Integer ONLINE = 1;

    /**
     * 挤下线
     */
    public static final Integer OFFLINE = 2;

    /**
     * 主动注销
     */
    public static final Integer LOGOUT = 3;

    /**
     *
     */
    private Long userId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 1/在线 2/挤下线 3/主动退出
     */
    private Integer onlineStatus;

    /**
     * 最后活跃的时间
     */
    private LocalDateTime lastActiveTime;

    @Override
    public String toString() {
        return "CacheUser{" +
                "userId=" + userId +
                ", tenantId=" + tenantId +
                ", onlineStatus=" + onlineStatus +
                ", lastActiveTime=" + lastActiveTime +
                '}';
    }

    public Long getTenantId() {
        return tenantId;
    }

    public CacheUser setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CacheUser setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public CacheUser setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
        return this;
    }

    public LocalDateTime getLastActiveTime() {
        return lastActiveTime;
    }

    public CacheUser setLastActiveTime(LocalDateTime lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
        return this;
    }
}
