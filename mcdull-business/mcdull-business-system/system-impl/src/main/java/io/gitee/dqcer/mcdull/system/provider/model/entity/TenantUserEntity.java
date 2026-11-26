package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户-用户关系
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_user")
public class TenantUserEntity extends BaseEntity<Integer> {

    private Integer tenantId;

    private Integer userId;
}
