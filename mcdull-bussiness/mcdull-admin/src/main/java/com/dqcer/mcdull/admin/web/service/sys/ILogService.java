package io.gitee.dqcer.admin.web.service.sys;

import io.gitee.dqcer.framework.base.vo.PagedVO;
import io.gitee.dqcer.framework.base.wrapper.Result;
import io.gitee.dqcer.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.admin.model.vo.sys.LogVO;

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
