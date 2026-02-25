package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocViewRecordEntity;

public interface IHelpDocViewRecordService extends IRepository<HelpDocViewRecordEntity> {

    /**
     * 分页
     *
     * @param dto dto
     * @return {@link Page}<{@link HelpDocViewRecordEntity}>
     */
    Page<HelpDocViewRecordEntity> selectPage(HelpDocViewRecordQueryDTO dto);
}
