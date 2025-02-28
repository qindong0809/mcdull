package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EnterpriseVO;

/**
 * Enterprise Service
 *
 * @author dqcer
 * @since 2024/7/25 9:26
 */

public interface IOaEnterpriseService {

    PagedVO<EnterpriseVO> queryByPage(EnterpriseQueryDTO dto);

    void add(EnterpriseAddDTO dto);

    void update(EnterpriseUpdateDTO dto);

    void delete(Integer enterpriseId);

    EnterpriseVO getDetail(Integer enterpriseId);

    void exportData(EnterpriseQueryDTO dto);
}
