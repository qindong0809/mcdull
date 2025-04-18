package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.EnterpriseVO;

/**
 * Enterprise Service
 *
 * @author dqcer
 * @since 2024/7/25 9:26
 */

public interface IOaEnterpriseService {

    /**
     * 按页查询
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link EnterpriseVO }>
     */
    PagedVO<EnterpriseVO> queryByPage(EnterpriseQueryDTO dto);

    /**
     * 加
     *
     * @param dto DTO
     */
    void add(EnterpriseAddDTO dto);

    /**
     * 更新
     *
     * @param dto DTO
     */
    void update(EnterpriseUpdateDTO dto);

    /**
     * 删除
     *
     * @param enterpriseId 企业 ID
     */
    void delete(Integer enterpriseId);

    /**
     * 获取详细信息
     *
     * @param enterpriseId 企业 ID
     * @return {@link EnterpriseVO }
     */
    EnterpriseVO getDetail(Integer enterpriseId);

    /**
     * 导出数据
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(EnterpriseQueryDTO dto);
}
