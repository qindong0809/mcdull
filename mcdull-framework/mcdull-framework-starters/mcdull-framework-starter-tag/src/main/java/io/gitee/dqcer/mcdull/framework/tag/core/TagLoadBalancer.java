package io.gitee.dqcer.mcdull.framework.tag.core;

import io.gitee.dqcer.mcdull.framework.tag.config.TagProperties;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextHolder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TagLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final String serviceId;
    private final ObjectProvider<ServiceInstanceListSupplier> supplierProvider;
    private final TagProperties tagProperties;

    public TagLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> supplierProvider,
                           String serviceId,
                           TagProperties tagProperties) {
        this.supplierProvider = supplierProvider;
        this.serviceId = serviceId;
        this.tagProperties = tagProperties;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = supplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(instances -> selectInstance(instances, request));
    }

    private Response<ServiceInstance> selectInstance(List<ServiceInstance> instances, Request request) {
        if (instances.isEmpty()) {
            return new EmptyResponse();
        }
        String targetTag = extractTag(request);

        // 1. 精准匹配带标签的实例（排除强制走基准服的保留字 default）
        if (StringUtils.hasText(targetTag) && !EnvTagContextHolder.DEFAULT_TAG.equalsIgnoreCase(targetTag)) {
            List<ServiceInstance> matched = instances.stream()
                .filter(ins -> targetTag.equals(ins.getMetadata().get(EnvTagContextHolder.META_ENV_TAG)))
                .toList();
            if (!matched.isEmpty()) {
                return randomPick(matched);
            }
            // 若开启严格模式且未找到目标标签实例，直接返回空响应阻断回流
            if (tagProperties.isStrict()) {
                return new EmptyResponse();
            }
        }

        // 2. 基准兜底：路由到无标签的公共服务器实例
        List<ServiceInstance> defaultInstances = instances.stream()
            .filter(ins -> !StringUtils.hasText(ins.getMetadata().get(EnvTagContextHolder.META_ENV_TAG)))
            .toList();
        if (!defaultInstances.isEmpty()) {
            return randomPick(defaultInstances);
        }

        // 3. 紧急容灾兜底
        return randomPick(instances);
    }

    private String extractTag(Request request) {
        if (request.getContext() instanceof RequestDataContext context) {
            String headerTag = context.getClientRequest().getHeaders().getFirst(EnvTagContextHolder.HEADER_ENV_TAG);
            if (StringUtils.hasText(headerTag)) {
                return headerTag;
            }
        }
        return EnvTagContextHolder.getTag();
    }

    private Response<ServiceInstance> randomPick(List<ServiceInstance> list) {
        return new DefaultResponse(list.get(ThreadLocalRandom.current().nextInt(list.size())));
    }
}
