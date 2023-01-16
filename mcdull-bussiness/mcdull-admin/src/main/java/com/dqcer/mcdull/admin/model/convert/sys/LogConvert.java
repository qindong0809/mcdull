package io.gitee.dqcer.admin.model.convert.sys;

import io.gitee.dqcer.admin.model.vo.sys.LogVO;
import io.gitee.dqcer.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.admin.model.entity.sys.LogDO;
import io.gitee.dqcer.framework.web.feign.model.LogOperationDTO;

/**
* 日志记录 对象转换工具类
*
* @author dqcer
* @version 2023-01-14
*/
public class LogConvert {


    public static LogDO convertToLogDO(LogOperationDTO dto) {
        if (dto == null) {
            return null;
        }
        LogDO logDO = new LogDO();
        logDO.setAccountId(dto.getAccountId());
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
        vo.setMenu(item.getMenu());
        vo.setModel(item.getModel());
        vo.setType(item.getType());

        return vo;
    }
    private LogConvert() {
    }
}