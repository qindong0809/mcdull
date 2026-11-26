package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户-菜单白名单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_menu")
public class TenantMenuEntity extends BaseEntity<Integer> {

    private Integer tenantId;

    private Integer menuId;
}
