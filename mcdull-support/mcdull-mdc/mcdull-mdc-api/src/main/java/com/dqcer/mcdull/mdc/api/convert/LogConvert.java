package com.dqcer.mcdull.mdc.api.convert;

import com.dqcer.mcdull.mdc.api.entity.LogDO;
import com.dqcer.mcdull.mdc.api.vo.DictVO;
import com.dqcer.mcdull.mdc.api.vo.LogVO;

public class LogConvert {


    /**
     * 实体转换来视图对象
     *
     * @param item 项
     * @return {@link DictVO}
     */
    public static LogVO entity2Vo(LogDO item) {
        LogVO logVO = new LogVO();
        logVO.setMobile(item.getMobile());
        logVO.setAccountId(item.getAccountId());
        logVO.setTenantId(item.getTenantId());
        logVO.setCreatedTime(item.getCreatedTime());
        logVO.setClientIp(item.getClientIp());
        logVO.setBrowser(item.getBrowser());
        logVO.setPlatform(item.getPlatform());
        logVO.setOs(item.getOs());
        logVO.setEngine(item.getEngine());
        logVO.setVersion(item.getVersion());
        logVO.setEngineVersion(item.getEngineVersion());
        logVO.setTime(item.getTime());
        logVO.setMethod(item.getMethod());
        logVO.setPath(item.getPath());
        logVO.setTimeTaken(item.getTimeTaken());
        logVO.setStatus(item.getStatus());
        logVO.setParameterMap(item.getParameterMap());
        logVO.setRequestBody(item.getRequestBody());
        logVO.setHeaders(item.getHeaders());
        logVO.setResponseBody(item.getResponseBody());
        logVO.setId(item.getId());
        return logVO;
    }
}
