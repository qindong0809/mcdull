package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeVisibleRangeService {

    void batchInsert(List<NoticeVisibleRangeEntity> list);

    List<NoticeVisibleRangeEntity> getListByNoticeId(Integer noticeId);

    void update(List<NoticeVisibleRangeEntity> insertList,
                List<NoticeVisibleRangeEntity> updateList,
                List<NoticeVisibleRangeEntity> removeList);
}
