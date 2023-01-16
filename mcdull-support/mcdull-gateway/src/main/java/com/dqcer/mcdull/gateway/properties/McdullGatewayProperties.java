package com.dqcer.mcdull.gateway.properties;

import com.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 网关属性
 *
 * @author dqcer
 * @version 2022/10/27
 */
@ConfigurationProperties(prefix = GlobalConstant.ROOT_PREFIX)
public class McdullGatewayProperties {

    /**
     * 启用日志
     */
    private Boolean enableLog = Boolean.TRUE;

    /**
     * 忽略
     */
    private FilterProperties filter = new FilterProperties();


    public Boolean getEnableLog() {
        return enableLog;
    }

    public void setEnableLog(Boolean enableLog) {
        this.enableLog = enableLog;
    }

    public FilterProperties getFilter() {
        return filter;
    }

    public void setFilter(FilterProperties filter) {
        this.filter = filter;
    }
}
