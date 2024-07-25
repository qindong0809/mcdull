package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocViewRecordEntity;

/**
 * Help  View Record Repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IHelpDocViewRecordRepository extends IService<HelpDocViewRecordEntity>  {

    /**
     * 分页
     *
     * @param dto dto
     * @return {@link Page}<{@link HelpDocViewRecordEntity}>
     */
    Page<HelpDocViewRecordEntity> selectPage(HelpDocViewRecordQueryDTO dto);
}