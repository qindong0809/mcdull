package com.dqcer.mcdull.admin.model.convert.sys;

import com.dqcer.mcdull.admin.model.vo.sys.LogVO;
import com.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import com.dqcer.mcdull.admin.model.entity.sys.LogDO;

/**
* 日志记录 对象转换工具类
*
* @author dqcer
* @version 2023-01-14
*/
public class LogConvert {

   /**
    * LogLiteDTO转换为LogDO
    *
    * @param item LogLiteDTO
    * @return {@link LogDO}
    */
    public static LogDO convertToLogDO(LogLiteDTO item){
        if (item == null){
            return null;
        }
        LogDO entity = new LogDO();
        entity.setId(item.getId());
        entity.setAccountId(item.getAccountId());
        entity.setTenantId(item.getTenantId());
        entity.setClientIp(item.getClientIp());
        entity.setUserAgent(item.getUserAgent());
        entity.setMethod(item.getMethod());
        entity.setPath(item.getPath());
        entity.setTraceId(item.getTraceId());
        entity.setTimeTaken(item.getTimeTaken());
        entity.setParameterMap(item.getParameterMap());
        entity.setHeaders(item.getHeaders());

        return entity;
    }


    /**
    * LogDOLogVO
    *
    * @param item LogDO
    * @return {@link LogVO}
    */
    public static LogVO convertToLogVO(LogDO item){
        if (item == null){
            return null;
        }
        LogVO vo = new LogVO();
        vo.setId(item.getId());
        vo.setAccountId(item.getAccountId());
        vo.setTenantId(item.getTenantId());
        vo.setClientIp(item.getClientIp());
        vo.setUserAgent(item.getUserAgent());
        vo.setMethod(item.getMethod());
        vo.setPath(item.getPath());
        vo.setTraceId(item.getTraceId());
        vo.setTimeTaken(item.getTimeTaken());
        vo.setParameterMap(item.getParameterMap());
        vo.setHeaders(item.getHeaders());

        return vo;
    }
    private LogConvert() {
    }
}