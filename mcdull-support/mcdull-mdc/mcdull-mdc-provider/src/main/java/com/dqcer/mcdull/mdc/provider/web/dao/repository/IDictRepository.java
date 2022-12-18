package com.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.mdc.provider.model.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.provider.model.entity.DictDO;

public interface IDictRepository extends IService<DictDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link DictDO}>
     */
    Page<DictDO> selectPage(DictLiteDTO dto);
}
