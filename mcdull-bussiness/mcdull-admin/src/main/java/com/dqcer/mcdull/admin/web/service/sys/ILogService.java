package com.dqcer.mcdull.admin.web.service.sys;

import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import com.dqcer.mcdull.admin.model.vo.sys.LogVO;

public interface ILogService {

    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result<PagedVO<LogVO>>}
     */
    Result<PagedVO<LogVO>> listByPage(LogLiteDTO dto);
}
