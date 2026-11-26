package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import io.gitee.dqcer.mcdull.system.provider.config.TenantInitProperties;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantInitProgressVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantTaskVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.TenantInitExecutor;
import io.gitee.dqcer.mcdull.system.provider.web.manager.TenantInitProgressPublisher;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantInitService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class TenantInitServiceImpl implements ITenantInitService {

    @Resource
    private TenantInitExecutor executor;
    @Resource
    private TenantInitProgressPublisher publisher;
    @Resource
    private TenantInitProperties properties;

    @Override
    public TenantTaskVO start(Integer tenantId, Boolean force) {
        return executor.start(tenantId, Boolean.TRUE.equals(force));
    }

    @Override
    public SseEmitter progress(String taskNo, Long lastEventId) {
        TenantInitProgressVO snapshot = executor.snapshot(taskNo);
        return publisher.subscribe(taskNo, lastEventId, snapshot, properties.getTaskTtlSeconds() * 1000);
    }

    @Override
    public TenantInitProgressVO snapshot(String taskNo) {
        return executor.snapshot(taskNo);
    }
}
