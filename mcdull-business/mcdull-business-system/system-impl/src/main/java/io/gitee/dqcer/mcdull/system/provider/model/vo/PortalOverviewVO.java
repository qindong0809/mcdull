package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PortalOverviewVO {
    private TenantVO tenant;
    private List<PortalAppVO> apps;
    private Map<String, Object> kpi;
    private List<Object> decisions;
    private List<Object> events;
}
