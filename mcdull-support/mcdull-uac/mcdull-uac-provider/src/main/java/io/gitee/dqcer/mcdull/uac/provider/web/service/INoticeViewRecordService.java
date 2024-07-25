package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeViewRecordEntity;

import java.util.List;

/**
 * Notice View Record Service
 *
 * @author dqcer
 * @since 2024/7/25 9:25
 */

public interface INoticeViewRecordService {

    NoticeViewRecordEntity getByUserIdAndNoticeId(Integer userId, Integer noticeId);

    void save(NoticeViewRecordEntity newEntity);

    void update(NoticeViewRecordEntity entity);

    List<Integer> getByUserId(Integer userId);
}
