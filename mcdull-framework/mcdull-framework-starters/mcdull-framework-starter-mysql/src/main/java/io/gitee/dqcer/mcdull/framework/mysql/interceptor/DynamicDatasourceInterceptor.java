package io.gitee.dqcer.mcdull.framework.mysql.interceptor;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 动态数据源拦截器
 *
 * @author dqcer
 * @since 2022/01/11
 */
@SuppressWarnings("unused")
public class DynamicDatasourceInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DataSourceProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenant = request.getHeader(HttpHeaderConstants.T_ID);
        boolean isNotEmpty = null != tenant && tenant.trim().length() > 0;
        if (log.isDebugEnabled()) {
            log.debug("DynamicDatasource Interceptor tenant : [{}]  default: [{}]", tenant, properties.getDefaultName());
        }
        if (isNotEmpty) {
            DynamicContextHolder.push(tenant);
        }
        // null底层已处理
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        DynamicContextHolder.clear();
    }
}
