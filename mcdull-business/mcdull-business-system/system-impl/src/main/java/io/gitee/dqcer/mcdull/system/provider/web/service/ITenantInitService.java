package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantInitProgressVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantTaskVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ITenantInitService {
    TenantTaskVO start(Integer tenantId, Boolean force);

    SseEmitter progress(String taskNo, Long lastEventId);

    TenantInitProgressVO snapshot(String taskNo);
}
