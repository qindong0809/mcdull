package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.config.log.IOperationLog;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.OperateLogVO;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IOperateLogService extends IOperationLog {

    PagedVO<OperateLogVO> queryByPage(OperateLogQueryDTO dto);

    OperateLogVO detail(Integer operateLogId);
}
