package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.service.TenantDataSourceApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteTenantDataSourceInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DatasourceEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.TenantInitStatusEnum;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDatasourceService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantAuthService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 租户数据源内部接口实现。
 *
 * @author dqcer
 */
@Primary
@RestController
public class TenantDataSourceApiImpl implements TenantDataSourceApi {

    @Resource
    private ITenantService tenantService;
    @Resource
    private IDatasourceService datasourceService;
    @Resource
    private ITenantAuthService tenantAuthService;

    @Override
    public InnerResult<RemoteTenantDataSourceInfoVO> getByTenantId(Integer tenantId) {
        if (tenantId == null || tenantId <= 0) {
            return InnerResult.error("system.tenant.required");
        }
        TenantEntity tenant = tenantService.getEntity(tenantId);
        if (tenant == null || Boolean.TRUE.equals(tenant.getDelFlag()) || Boolean.TRUE.equals(tenant.getInactive())
                || !TenantInitStatusEnum.SUCCESS.getCode().equals(tenant.getInitStatus())) {
            return InnerResult.error("system.tenant.forbidden");
        }
        DatasourceEntity datasource = datasourceService.getEntity(tenant.getDatasourceId());
        if (datasource == null || Boolean.TRUE.equals(datasource.getDelFlag()) || Boolean.TRUE.equals(datasource.getInactive())) {
            return InnerResult.error("system.datasource.forbidden");
        }

        String databaseName = tenant.getDatabaseName() == null || tenant.getDatabaseName().isBlank()
                ? "tenant_" + tenantId
                : tenant.getDatabaseName();
        RemoteTenantDataSourceInfoVO vo = new RemoteTenantDataSourceInfoVO();
        vo.setKey("tenant_" + tenantId);
        vo.setTenantId(tenantId);
        vo.setUrl(tenantUrl(datasource.getJdbcUrl(), databaseName));
        vo.setUsername(datasource.getUsername());
        // 只返回密文：缓存 VO 不得含明文，由 resolver 使用时解密
        vo.setPassword(datasource.getPasswordCipher());
        return InnerResult.success(vo);
    }

    @Override
    public InnerResult<Set<Integer>> getAuthorizedTenantIds(String userId) {
        if (userId == null || userId.isBlank()) {
            return InnerResult.success(Set.of());
        }
        try {
            return InnerResult.success(tenantAuthService.authorizedTenantIds(Integer.valueOf(userId)));
        } catch (NumberFormatException ex) {
            return InnerResult.error("system.tenant.required");
        }
    }

    private String tenantUrl(String jdbcUrl, String databaseName) {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            return jdbcUrl;
        }
        String query = "";
        String base = jdbcUrl;
        int q = base.indexOf('?');
        if (q > 0) {
            query = base.substring(q);
            base = base.substring(0, q);
        }
        int scheme = base.indexOf("//");
        int slash = scheme < 0 ? -1 : base.indexOf('/', scheme + 2);
        if (slash < 0) {
            return base + "/" + databaseName + query;
        }
        return base.substring(0, slash + 1) + databaseName + query;
    }
}
