package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeVO;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeService {

    void insert(NoticeAddDTO dto);

    void update(NoticeUpdateDTO dto);

    NoticeVO detail(Integer id);

    void batchDelete(List<Integer> idList);

    PagedVO<NoticeVO> queryPage(NoticeQueryDTO dto);
}
