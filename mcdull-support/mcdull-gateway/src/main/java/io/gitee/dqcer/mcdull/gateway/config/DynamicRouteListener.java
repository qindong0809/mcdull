package io.gitee.dqcer.mcdull.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
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
 * @since 2022/10/26
 */
@Component
public class DynamicRouteListener implements InitializingBean, ApplicationEventPublisherAware {

    private static final Logger log = LoggerFactory.getLogger(DynamicRouteListener.class);

    private static final String GATEWAY_ROUTES = "gateway-routes.json";

    @Resource
    private NacosConfigManager nacosConfigManager;

    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    private static final List<String> ROUTE_IDS = new ArrayList<>();

    private ApplicationEventPublisher applicationEventPublisher;

    @SuppressWarnings("all")
    @Override
    public void afterPropertiesSet() throws Exception {

        ConfigService configService = nacosConfigManager.getConfigService();
        if (null == configService || null == nacosConfigProperties) {
            log.error("动态路由侦听失败");
            return;
        }

        // 程序启动时，先读取指定的nacos的配置文件，采用子线程防止阻塞
        new Thread(() -> {
            String config = null;
            try {
                log.info("Loading Gateway Route File: {}", GATEWAY_ROUTES);
                config = nacosConfigManager.getConfigService().getConfig(GATEWAY_ROUTES, nacosConfigProperties.getGroup(), 3000);
            } catch (NacosException e) {
                log.error("nacos exception", e);
            }
            refresh(config);
        }).start();


        // 添加监听 指定的nacos的配置文件 事件
        configService.addListener(GATEWAY_ROUTES, nacosConfigProperties.getGroup(), new Listener() {
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
        if (StrUtil.isBlank(configInfo)) {
            log.warn("Read File is null");
            return;
        }
        log.info("配置信息 \n {}", configInfo);
        // 删除之前的路由
        for (String routeId : ROUTE_IDS) {
            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        }
        ROUTE_IDS.clear();

        JSONArray objects = JSONUtil.parseArray(configInfo);
        List<RouteDefinition> routeDefinitions = objects.toList(RouteDefinition.class);

        if (CollUtil.isEmpty(routeDefinitions)) {
            return;
        }
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
