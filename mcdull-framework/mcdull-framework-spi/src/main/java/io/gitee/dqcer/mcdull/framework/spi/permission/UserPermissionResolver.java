package io.gitee.dqcer.mcdull.framework.spi.permission;

/**
 * 用户权限数据源 SPI。
 * <p>
 * Provider: 业务侧（system-facade 的 Feign 适配器），实现"远程取权限 + 缓存 + 降级"；
 * Consumer: starter-security 的通用 StpInterface。
 * <p>
 * 一次 {@link #resolve(Object, String)} 同时返回权限与角色，避免同一请求内
 * checkPermission 与 checkRole 各打一次远程调用。未配置实现时使用 {@link #NOOP}，
 * 框架据此判定该服务不做功能级权限校验（全部拒绝）。
 *
 * @author dqcer
 */
public interface UserPermissionResolver {

    /** 未配置实现时的空实现：权限与角色均为空 */
    UserPermissionResolver NOOP = (loginId, loginType) -> UserPermissionInfo.EMPTY;

    /**
     * 取用户的权限与角色信息。
     *
     * @param loginId   登录 ID
     * @param loginType 账号体系标识
     * @return 权限信息，不得返回 null；取数失败时返回 {@link UserPermissionInfo#EMPTY}
     */
    UserPermissionInfo resolve(Object loginId, String loginType);
}
