package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.OaEnterpriseEntity;

/**
 * Enterprise repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IOaEnterpriseRepository extends IRepository<OaEnterpriseEntity> {

    /**
     * 分页
     *
     * @param dto dto
     * @return {@code Page<OaEnterpriseEntity>}
     */
    Page<OaEnterpriseEntity> selectPage(EnterpriseQueryDTO dto);
}