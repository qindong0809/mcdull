package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.alibaba.fastjson2.JSON;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.PortalAppVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.PortalOverviewVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantVO;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IPortalService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortalServiceImpl implements IPortalService {

    private static final List<PortalAppVO> APP_REGISTRY = List.of(
            app("system", "系统设置", "system", "platform", "tool", "settings", true),
            app("workflow", "工作流", "workflow", "tenant", "business", "workflow", true),
            app("ai", "智能助手", "ai", "tenant", "ai", "ai", false),
            app("gov", "Agent 治理", "system", "platform", "tool", "gov", false),
            app("form", "表单设计器", "form", "tenant", "business", "workflow", false),
            app("monitor", "流程监控", "monitor", "tenant", "business", "workflow", false),
            app("dict", "数据字典", "system", "platform", "tool", "settings", false)
    );

    @Resource
    private TenantMapper tenantMapper;

    @Override
    public PortalOverviewVO overview(Integer tenantId) {
        PortalOverviewVO vo = new PortalOverviewVO();
        TenantEntity entity = tenantId == null ? null : tenantMapper.selectById(tenantId);
        if (entity != null) {
            TenantVO tenant = new TenantVO();
            tenant.setId(entity.getId());
            tenant.setTenantName(entity.getTenantName());
            tenant.setApplicationCode(entity.getApplicationCode() == null ? Collections.emptyList()
                    : JSON.parseArray(entity.getApplicationCode(), String.class));
            vo.setTenant(tenant);
        }
        Set<String> apps = entity == null || entity.getApplicationCode() == null
                ? Collections.emptySet()
                : new HashSet<>(JSON.parseArray(entity.getApplicationCode(), String.class));
        vo.setApps(APP_REGISTRY.stream()
                .filter(a -> "platform".equals(a.getScope()) || apps.contains(a.getApplicationCode()))
                .collect(Collectors.toList()));
        Map<String, Object> kpi = new HashMap<>();
        kpi.put("decision", 0);
        kpi.put("agent", 0);
        kpi.put("running", 0);
        kpi.put("cost", "0.0");
        vo.setKpi(kpi);
        vo.setDecisions(Collections.emptyList());
        vo.setEvents(Collections.emptyList());
        return vo;
    }

    private static PortalAppVO app(String id, String name, String applicationCode, String scope,
                                   String group, String target, boolean favorite) {
        PortalAppVO vo = new PortalAppVO();
        vo.setId(id);
        vo.setName(name);
        vo.setApplicationCode(applicationCode);
        vo.setScope(scope);
        vo.setGroup(group);
        vo.setTarget(target);
        vo.setFavorite(favorite);
        return vo;
    }
}
