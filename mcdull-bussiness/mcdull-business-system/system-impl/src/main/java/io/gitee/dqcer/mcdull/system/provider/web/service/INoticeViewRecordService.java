package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeViewRecordEntity;

import java.util.List;

/**
 * Notice View Record Service
 *
 * @author dqcer
 * @since 2024/7/25 9:25
 */

public interface INoticeViewRecordService extends IRepository<NoticeViewRecordEntity> {

    /**
     * get by user id and notice id。
     *
     * @param userId   userId
     * @param noticeId noticeId
     * @return {@link NoticeViewRecordEntity }
     */
    NoticeViewRecordEntity getByUserIdAndNoticeId(Integer userId, Integer noticeId);

    /**
     * update
     *
     * @param entity entity
     */
    void update(NoticeViewRecordEntity entity);

    /**
     * get
     *
     * @param userId userId
     * @return {@link List }<{@link Integer }>
     */
    List<Integer> getByUserId(Integer userId);
}
