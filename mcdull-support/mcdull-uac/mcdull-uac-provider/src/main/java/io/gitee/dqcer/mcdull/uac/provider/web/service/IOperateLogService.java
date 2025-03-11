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

    /**
     * 按页查询
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link OperateLogVO }>
     */
    PagedVO<OperateLogVO> queryByPage(OperateLogQueryDTO dto);

    /**
     * 详情
     *
     * @param operateLogId 作日志 ID
     * @return {@link OperateLogVO }
     */
    OperateLogVO detail(Integer operateLogId);

    /**
     * 首页
     *
     * @return {@link KeyValueVO }<{@link List }<{@link String }>, {@link List }<{@link Integer }>>
     */
    KeyValueVO<List<String>, List<Integer>> homePie();

    /**
     * 首页
     *
     * @return {@link List }<{@link NameValueVO }<{@link String }, {@link Integer }>>
     */
    List<NameValueVO<String, Integer>> pieHome();

    /**
     * 导出数据
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(OperateLogQueryDTO dto);
}
