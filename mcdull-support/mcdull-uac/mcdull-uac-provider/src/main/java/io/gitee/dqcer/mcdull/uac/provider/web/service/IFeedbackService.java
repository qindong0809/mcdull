package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FeedbackVO;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IFeedbackService {


    PagedVO<FeedbackVO> query(FeedbackQueryDTO dto);

    void add(FeedbackAddDTO dto);
}