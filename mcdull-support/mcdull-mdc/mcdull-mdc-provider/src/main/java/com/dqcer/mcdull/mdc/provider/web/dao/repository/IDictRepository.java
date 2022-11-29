package com.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.SysDictEntity;

public interface IDictRepository extends IService<SysDictEntity> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link SysDictEntity}>
     */
    Page<SysDictEntity> selectPage(DictLiteDTO dto);
}
