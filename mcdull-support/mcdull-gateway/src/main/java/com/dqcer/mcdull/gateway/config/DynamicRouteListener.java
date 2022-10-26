package com.dqcer.mcdull.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 动态路由侦听器
 [
    {
        "id":"ecc-platform",
        "uri":"lb://platform-system-provider",
        "predicates":[
            {
                "args":{
                    "partten":"/plt/**"
                },
                "name":"Path"
            }
        ],
        "filters":[
            {
                "args":{
                    "_genkey_0":"1"
                },
                "name":"StripPrefix"
            }
        ]
    },
    {
        "id":"mds-core",
        "uri":"lb://mds-core-provider",
        "predicates":[
            {
                "args":{
                    "partten":"/mdscore/**"
                },
                "name":"Path"
            }
        ],
        "filters":[
            {
                "args":{
                    "_genkey_0":"1"
                },
                "name":"StripPrefix"
            }
        ]
    }
]



 *
 * @author dqcer
 * @date 2022/10/26
 */
@Component
public class DynamicRouteListener implements InitializingBean, ApplicationEventPublisherAware {

    private static final Logger log = LoggerFactory.getLogger(DynamicRouteListener.class);

    @Value("${route.dataId:route}")
    private String routeDataId;

    @Resource
    private NacosConfigManager nacosConfigManager;

    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    private static final List<String> ROUTE_IDS = new ArrayList<>();

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigService configService = nacosConfigManager.getConfigService();
        if (null == configService || null == nacosConfigProperties) {
            log.error("动态路由侦听失败");
            return;
        }

        // 程序启动时，先读取指定的nacos的配置文件
        String config = nacosConfigManager.getConfigService().getConfig(routeDataId, nacosConfigProperties.getGroup(), 3000);
        refresh(config);

        // 添加监听 指定的nacos的配置文件 事件
        configService.addListener(routeDataId, nacosConfigProperties.getGroup(), new Listener() {
            @Override
            public Executor getExecutor() {
                return new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500), r -> {
                    Thread thread = new Thread(r);
                    thread.setName("route-" + r.hashCode());
                    return thread;
                });
            }
            @Override
            public void receiveConfigInfo(String configInfo) {
                refresh(configInfo);
            }
        });
    }

    /**
     * refresh
     *
     * @param configInfo 配置信息
     */
    private void refresh(String configInfo) {
        // 删除之前的路由
        for (String routeId : ROUTE_IDS) {
            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        }
        ROUTE_IDS.clear();

        List<RouteDefinition> routeDefinitions = JSONObject.parseArray(configInfo, RouteDefinition.class);
        for (RouteDefinition routeDefinition : routeDefinitions) {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            ROUTE_IDS.add(routeDefinition.getId());
        }
        // 发布路由
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
