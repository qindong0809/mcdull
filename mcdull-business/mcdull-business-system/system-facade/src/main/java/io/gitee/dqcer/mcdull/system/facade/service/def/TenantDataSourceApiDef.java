package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteTenantDataSourceVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 租户数据源契约定义。
 *
 * @author dqcer
 */
public interface TenantDataSourceApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/tenant-datasource/by-tenant")
    InnerResult<RemoteTenantDataSourceVO> getByTenantId(@RequestParam("tenantId") Integer tenantId);

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/tenant-datasource/authorized-tenants")
    InnerResult<Set<Integer>> getAuthorizedTenantIds(@RequestParam("userId") String userId);
}
