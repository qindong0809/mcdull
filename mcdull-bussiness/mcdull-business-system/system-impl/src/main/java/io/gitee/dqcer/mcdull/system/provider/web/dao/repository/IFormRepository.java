package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FormQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormEntity;


/**
 * Form repository
 *
 * @author dqcer
 * @since 2024/7/25 11:24
 */

public interface IFormRepository extends IRepository<FormEntity> {

    /**
     * 分页
     *
     * @param dto dto
     * @return {@code Page<FormEntity>}
     */
    Page<FormEntity> selectPage(FormQueryDTO dto);

    /**
     * 根据名称查询
     *
     * @param name 名称
     * @return {@code FormEntity}
     */
    FormEntity getByName(String name);
}