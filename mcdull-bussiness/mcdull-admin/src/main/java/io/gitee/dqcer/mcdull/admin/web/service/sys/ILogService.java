package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LogVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
 * log 服务
 *
 * @author dqcer
 * @since 2023/01/15 13:01:34
 */
public interface ILogService {

    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result<PagedVO< LogVO >>}
     */
    Result<PagedVO<LogVO>> listByPage(LogLiteDTO dto);
}
