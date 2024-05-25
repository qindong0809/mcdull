package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeViewRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeViewRecordService {

    NoticeViewRecordEntity getByUserIdAndNoticeId(Integer userId, Integer noticeId);

    void save(NoticeViewRecordEntity newEntity);

    void update(NoticeViewRecordEntity entity);

    List<Integer> getByUserId(Integer userId);
}
