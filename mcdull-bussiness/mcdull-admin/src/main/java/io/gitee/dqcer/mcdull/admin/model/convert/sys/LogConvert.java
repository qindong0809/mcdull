package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.vo.sys.LogVO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogDO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;

/**
* 日志记录 对象转换工具类
*
* @author dqcer
* @since 2023-01-14
*/
public class LogConvert {


    public static LogDO convertToLogDO(LogOperationDTO dto) {
        if (dto == null) {
            return null;
        }
        LogDO logDO = new LogDO();
        logDO.setAccountId(dto.getUserId());
        logDO.setTenantId(dto.getTenantId());
        logDO.setClientIp(dto.getClientIp());
        logDO.setUserAgent(dto.getUserAgent());
        logDO.setMethod(dto.getMethod());
        logDO.setPath(dto.getPath());
        logDO.setTraceId(dto.getTraceId());
        logDO.setTimeTaken(dto.getTimeTaken());
        logDO.setParameterMap(dto.getParameterMap());
        logDO.setHeaders(dto.getHeaders());
        logDO.setCreatedTime(dto.getCreatedTime());
        return logDO;

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
        vo.setButton(item.getButton());
        vo.setCreatedTime(item.getCreatedTime());

        return vo;
    }
    private LogConvert() {
    }
}