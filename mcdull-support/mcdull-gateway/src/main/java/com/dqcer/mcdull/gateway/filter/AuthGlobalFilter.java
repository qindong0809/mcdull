package com.dqcer.mcdull.gateway.filter;

import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.constants.SysConstants;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.gateway.properties.McdullGatewayProperties;
import com.dqcer.mcdull.gateway.utils.IpUtils;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * 认证过滤器
 *
 * @author dongqin
 * @date 2022/10/27
 */
@Component
public class AuthGlobalFilter extends AbstractFilter implements GlobalFilter, Ordered {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Resource
    private McdullGatewayProperties mcdullGatewayProperties;

    private static final Logger log = LoggerFactory.getLogger(AuthGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest.Builder mutate = request.mutate();
        HttpHeaders headers = request.getHeaders();
        String realIp = IpUtils.getRealIp(request);
        String requestUrl = exchange.getRequest().getURI().getPath();

        log.info("请求地址: {} 来源Ip: {}", requestUrl, realIp);

        // 无需效验的接口
        if (ignoreFilter(request.getPath().toString(), mcdullGatewayProperties.getFilter().getNoAuth())) {
            if (log.isDebugEnabled()) {
                log.debug("Gateway Filer ignore url：{}", request.getPath());
            }
            return chain.filter(exchange);
        }

        // feign拦截
        if(requestUrl.toLowerCase().contains(SysConstants.FEIGN_URL)) {
            log.warn("内部feign接口，外部非法调用");
            return errorResponse(response, ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
        }

        //  租户id效验
        String tenantIdStr = headers.getFirst(HttpHeaderConstants.T_ID);
        if (log.isDebugEnabled()) {
            log.debug("Gateway Filer TenantId: {}", tenantIdStr);
        }
        if (null == tenantIdStr || tenantIdStr.trim().length() == 0) {
            log.error("头部tid参数缺失");
            return errorResponse(response, ResultCode.ERROR_PARAMETERS.getCode(), ResultCode.ERROR_PARAMETERS.getMessage());
        }
        if (!isNumber(tenantIdStr)) {
            log.error("头部tid参数异常");
            return errorResponse(response, ResultCode.ERROR_PARAMETERS.getCode(), ResultCode.ERROR_PARAMETERS.getMessage());
        }

        // token 效验
        String authorization = headers.getFirst(HttpHeaderConstants.AUTHORIZATION);
        if (log.isDebugEnabled()) {
            log.debug("Gateway Filer Authorization: {}", authorization);
        }
        if (null == authorization || authorization.trim().length() == 0) {
            log.error("头部Authorization参数缺失");
            return errorResponse(response, ResultCode.UN_AUTHORIZATION.getCode(), ResultCode.UN_AUTHORIZATION.getMessage());
        }

        String token = authorization.substring(HttpHeaderConstants.BEARER.length());
        if (null == token || token.trim().length() == 0) {
            log.error("头部Authorization参数缺失Bearer ");
            return errorResponse(response, ResultCode.UN_AUTHORIZATION.getCode(), ResultCode.UN_AUTHORIZATION.getMessage());
        }


        // TODO: 2022/10/27 根据token验证身份

        addHeader(mutate, HttpHeaderConstants.T_ID, tenantIdStr);


        return chain.filter(exchange.mutate().request(mutate.build()).build());
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
