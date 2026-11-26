package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TenantDetailVO extends TenantVO {
    private Integer userCount;
    private Integer menuCount;
}
