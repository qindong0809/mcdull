package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.LogDO;

/**
 * log 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface ILogRepository extends IService<LogDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link LogDO}>
     */
    Page<LogDO> selectPage(LogLiteDTO dto);
}
