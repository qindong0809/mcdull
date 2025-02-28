package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.NameValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.config.log.IOperationLog;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.OperateLogVO;

import java.util.List;

/**
 * Operate Log Service
 *
 * @author dqcer
 * @since 2024/7/25 9:26
 */

public interface IOperateLogService extends IOperationLog {

    PagedVO<OperateLogVO> queryByPage(OperateLogQueryDTO dto);

    OperateLogVO detail(Integer operateLogId);

    KeyValueVO<List<String>, List<Integer>> home();

    List<NameValueVO<String, Integer>> pieHome();

    void exportData(OperateLogQueryDTO dto);
}
