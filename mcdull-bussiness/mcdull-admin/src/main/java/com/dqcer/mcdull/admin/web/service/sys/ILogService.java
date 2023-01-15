package com.dqcer.mcdull.admin.web.service.sys;

import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import com.dqcer.mcdull.admin.model.vo.sys.LogVO;
import com.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;

/**
 * log 服务
 *
 * @author dqcer
 * @date 2023/01/15 13:01:34
 */
public interface ILogService {

    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result<PagedVO<LogVO>>}
     */
    Result<PagedVO<LogVO>> listByPage(LogLiteDTO dto);
}
