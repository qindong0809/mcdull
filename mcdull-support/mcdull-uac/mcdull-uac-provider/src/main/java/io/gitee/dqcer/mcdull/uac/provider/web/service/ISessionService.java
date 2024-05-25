package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SessionQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.SessionVO;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface ISessionService {

    PagedVO<SessionVO> queryPage(SessionQueryDTO dto);

    void batchKickout(List<Integer> loginIdList);
}
