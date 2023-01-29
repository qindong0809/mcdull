package io.gitee.dqcer.mcdull.gateway.filter;

import io.gitee.dqcer.mcdull.gateway.properties.FilterProperties;
import io.gitee.dqcer.mcdull.gateway.properties.McdullGatewayProperties;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.gateway.utils.SpringUtils;
import io.gitee.dqcer.mcdull.uac.client.service.AuthClientService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 认证过滤器
 *
 * @author dqcer
 * @since  2022/10/27
 */
@Component
public class AuthFilter extends AbstractFilter implements GlobalFilter, Ordered {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 3000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadFactoryBuilder()
            .setNameFormat("auth-pool-%d").build());


    @Resource
    private McdullGatewayProperties mcdullGatewayProperties;

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest.Builder mutate = request.mutate();
        HttpHeaders headers = request.getHeaders();
        String requestUrl = exchange.getRequest().getURI().getPath();

        FilterProperties filterProperties = mcdullGatewayProperties.getFilter();

        // 无需效验的接口
        if (ignoreFilter(request.getPath().toString(), filterProperties.getNoAuth())) {
            if (log.isDebugEnabled()) {
                log.debug("Gateway Filer ignore url：{}", request.getPath());
            }
            return chain.filter(exchange);
        }

        // feign拦截
        if(requestUrl.toLowerCase().contains(GlobalConstant.INNER_API)) {
            log.warn("内部feign接口，外部非法调用");
            return errorResponse(response, CodeEnum.NOT_FOUND.getCode(), CodeEnum.NOT_FOUND.getMessage());
        }

        //  租户id效验
        Boolean enableMultiTenant = filterProperties.getEnableMultiTenant();
        if (enableMultiTenant) {
            String tenantIdStr = headers.getFirst(HttpHeaderConstants.T_ID);
            if (log.isDebugEnabled()) {
                log.debug("Gateway Filer TenantId: {}", tenantIdStr);
            }
            if (null == tenantIdStr || tenantIdStr.trim().length() == 0) {
                log.error("头部tid参数缺失");
                return errorResponse(response, CodeEnum.ERROR_PARAMETERS.getCode(), CodeEnum.ERROR_PARAMETERS.getMessage());
            }
            if (!isNumber(tenantIdStr)) {
                log.error("头部tid参数异常");
                return errorResponse(response, CodeEnum.ERROR_PARAMETERS.getCode(), CodeEnum.ERROR_PARAMETERS.getMessage());
            }
            addHeader(mutate, HttpHeaderConstants.T_ID, tenantIdStr);
        }

        // token 效验
        String authorization = headers.getFirst(HttpHeaderConstants.AUTHORIZATION);
        if (log.isDebugEnabled()) {
            log.debug("Gateway Filer Authorization: {}", authorization);
        }
        if (null == authorization || authorization.trim().length() == 0) {
            log.error("头部Authorization参数缺失");
            return errorResponse(response, CodeEnum.UN_AUTHORIZATION.getCode(), CodeEnum.UN_AUTHORIZATION.getMessage());
        }

        String token = authorization.substring(HttpHeaderConstants.BEARER.length());
        if (token.trim().length() == 0) {
            log.error("头部Authorization参数缺失Bearer ");
            return errorResponse(response, CodeEnum.UN_AUTHORIZATION.getCode(), CodeEnum.UN_AUTHORIZATION.getMessage());
        }

        String traceId = headers.getFirst(HttpHeaderConstants.TRACE_ID_HEADER);
//        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        Result<Long> result = remoteValid(token, traceId);


        log.info("token valid result: {}", result);
        if (!result.isOk()) {
            return errorResponse(response, result.getCode(), result.getMessage());
        }
        addHeader(mutate, HttpHeaderConstants.U_ID, result.getData());
        // 暂定退出登录使用
        addHeader(mutate, HttpHeaderConstants.TOKEN, token);

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    /**
     * 远程调用验证
     *
     * @param token 令牌
     * @return {@link Result}<{@link Long}>
     */
    private static Result<Long> remoteValid(String token, String traceId) {
        // 身份验证服务,因网关加载顺序需要进行懒加载
        AuthClientService authClientService = SpringUtils.getBean(AuthClientService.class);

        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        // WebFlux异步调用，同步会报错
        Future<Result<Long>> future = executorService.submit(() -> authClientService.tokenValid(token, traceId));
        Result<Long> result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 忽略Filter
     *
     * @param path 路径
     * @return boolean
     */
    public boolean ignoreFilter(String path, List<String> list) {
        return list.stream().anyMatch(url -> path.startsWith(url) || PATH_MATCHER.match(url, path));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
