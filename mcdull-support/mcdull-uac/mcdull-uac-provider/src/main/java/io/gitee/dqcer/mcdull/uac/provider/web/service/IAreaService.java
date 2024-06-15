package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.AreaVO;

public interface IAreaService {

    PagedVO<AreaVO> queryPage(AreaQueryDTO dto);
}
