package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.NameValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.config.log.IOperationLog;
import io.gitee.dqcer.mcdull.system.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.OperateLogVO;

import java.util.List;

/**
 * Operate Log Service
 *
 * @author dqcer
 * @since 2024/7/25 9:26
 */

public interface IOperateLogService extends IRepository<OperateLogEntity> {

    /**
     * query page
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link OperateLogVO }>
     */
    PagedVO<OperateLogVO> queryByPage(OperateLogQueryDTO dto);

    /**
     * detail
     *
     * @param operateLogId operateLogId
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
     * pie home
     *
     * @return {@link List }<{@link NameValueVO }<{@link String }, {@link Integer }>>
     */
    List<NameValueVO<String, Integer>> pieHome();

    /**
     * export data
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(OperateLogQueryDTO dto);
}
