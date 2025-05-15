package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeDetailVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeVO;

import java.util.List;

/**
 * Notice Service
 *
 * @author dqcer
 * @since 2024/7/25 9:25
 */

public interface INoticeService {

    void insert(NoticeAddDTO dto);

    void update(NoticeUpdateDTO dto);

    NoticeVO detail(Integer id);

    void batchDelete(List<Integer> idList);

    PagedVO<NoticeVO> queryPage(NoticeQueryDTO dto);

    NoticeUpdateFormVO getUpdateFormVO(Integer noticeId);

    PagedVO<NoticeUserVO> queryUserNotice(NoticeEmployeeQueryDTO dto);

    NoticeDetailVO view(Integer noticeId);

    boolean exportData(NoticeQueryDTO dto);
}
