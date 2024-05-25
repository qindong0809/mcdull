package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EnterpriseVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeVO;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IOaEnterpriseService {

    PagedVO<EnterpriseVO> queryByPage(EnterpriseQueryDTO dto);

    void add(EnterpriseAddDTO dto);

    void update(EnterpriseUpdateDTO dto);

    void delete(Integer enterpriseId);
}
