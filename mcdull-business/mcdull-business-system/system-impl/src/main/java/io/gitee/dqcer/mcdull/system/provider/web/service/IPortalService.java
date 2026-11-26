package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.vo.PortalOverviewVO;

public interface IPortalService {
    PortalOverviewVO overview(Integer tenantId);
}
