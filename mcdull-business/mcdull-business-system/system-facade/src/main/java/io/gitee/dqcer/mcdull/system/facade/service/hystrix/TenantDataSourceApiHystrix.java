package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.service.TenantDataSourceApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteTenantDataSourceInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 租户数据源 Feign 降级：system 不可用或接口未实现时返回失败，
 * 由 SPI 适配器捕获后降级为不切租户库。
 *
 * @author dqcer
 */
@Slf4j
@Component
public class TenantDataSourceApiHystrix implements TenantDataSourceApi {

    @Override
    public InnerResult<RemoteTenantDataSourceInfoVO> getByTenantId(Integer tenantId) {
        LogHelp.error(log, "TenantDataSourceApiHystrix#getByTenantId: {}", tenantId);
        return InnerResult.hystrixFallBack();
    }

    @Override
    public InnerResult<Set<Integer>> getAuthorizedTenantIds(String userId) {
        LogHelp.error(log, "TenantDataSourceApiHystrix#getAuthorizedTenantIds: {}", userId);
        return InnerResult.hystrixFallBack();
    }
}
