package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeVisibleRangeEntity;

import java.util.List;

/**
 * Notice visible range service
 *
 * @author dqcer
 * @since 2024/7/25 9:26
 */

public interface INoticeVisibleRangeService extends IRepository<NoticeVisibleRangeEntity> {

    /**
     * batch insert
     *
     * @param list list
     */
    void batchInsert(List<NoticeVisibleRangeEntity> list);

    /**
     * get
     *
     * @param noticeId noticeId
     * @return {@link List }<{@link NoticeVisibleRangeEntity }>
     */
    List<NoticeVisibleRangeEntity> getListByNoticeId(Integer noticeId);

    /**
     * update
     *
     * @param insertList insertList
     * @param updateList updateList
     * @param removeList removeList
     */
    void update(List<NoticeVisibleRangeEntity> insertList,
                List<NoticeVisibleRangeEntity> updateList,
                List<NoticeVisibleRangeEntity> removeList);
}
