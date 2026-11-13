package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.OaEnterpriseEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.EnterpriseVO;

/**
 * Enterprise Service
 *
 * @author dqcer
 * @since 2024/7/25 9:26
 */

public interface IOaEnterpriseService extends IRepository<OaEnterpriseEntity> {

    /**
     * query page
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link EnterpriseVO }>
     */
    PagedVO<EnterpriseVO> queryByPage(EnterpriseQueryDTO dto);

    /**
     * add
     *
     * @param dto DTO
     */
    void add(EnterpriseAddDTO dto);

    /**
     * update
     *
     * @param dto DTO
     */
    void update(EnterpriseUpdateDTO dto);

    /**
     * delete
     *
     * @param enterpriseId enterpriseId
     */
    void delete(Integer enterpriseId);

    /**
     * get
     *
     * @param enterpriseId enterpriseId
     * @return {@link EnterpriseVO }
     */
    EnterpriseVO getDetail(Integer enterpriseId);

    /**
     * export data
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(EnterpriseQueryDTO dto);
}
