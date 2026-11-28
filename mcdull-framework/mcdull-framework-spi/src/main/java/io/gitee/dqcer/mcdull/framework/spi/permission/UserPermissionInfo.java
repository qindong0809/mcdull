package io.gitee.dqcer.mcdull.framework.spi.permission;

import java.util.List;

/**
 * 用户权限信息契约。
 * <p>
 * Provider: 业务侧（system-facade 的 Feign 适配器），由 RPC 返回的 VO 实现；
 * Consumer: starter-security 的通用 StpInterface。
 * <p>
 * 框架只消费权限码与角色码两个列表，不关心数据从哪来。实现方须保证
 * getter 返回非 null 的列表。
 *
 * @author dqcer
 */
public interface UserPermissionInfo {

    /** 空实现：权限与角色均为空 */
    UserPermissionInfo EMPTY = new UserPermissionInfo() {
        @Override
        public List<String> getPermissionList() {
            return List.of();
        }

        @Override
        public List<String> getRoleList() {
            return List.of();
        }
    };

    /**
     * 权限码列表
     */
    List<String> getPermissionList();

    /**
     * 角色码列表
     */
    List<String> getRoleList();
}
