package io.gitee.dqcer.mcdull.mdc.provider.model.convert;

import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.LogEntity;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.LogVO;

/**
 * log 转换工具类
 *
 * @author dqcedr
 * @since 2022/12/26
 */
public class LogConvert {


    /**
     * 实体转换来视图对象
     *
     * @param item 项
     * @return {@link DictTypeClientVO}
     */
    public static LogVO entity2Vo(LogEntity item) {
        LogVO logVO = new LogVO();
        logVO.setAccountId(item.getAccountId());
        logVO.setTenantId(item.getTenantId());
        logVO.setCreatedTime(item.getCreatedTime());
        logVO.setClientIp(item.getClientIp());
        logVO.setUserAgent(item.getUserAgent());
        logVO.setMethod(item.getMethod());
        logVO.setPath(item.getPath());
        logVO.setTimeTaken(item.getTimeTaken());
        logVO.setParameterMap(item.getParameterMap());
        logVO.setHeaders(item.getHeaders());
        logVO.setTraceId(item.getTraceId());
        logVO.setId(item.getId());
        return logVO;
    }
}
