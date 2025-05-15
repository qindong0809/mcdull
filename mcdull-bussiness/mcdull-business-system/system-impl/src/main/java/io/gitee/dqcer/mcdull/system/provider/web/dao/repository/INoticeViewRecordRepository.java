package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeViewRecordEntity;

import java.util.List;

/**
 * Notice View Record Repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface INoticeViewRecordRepository extends IRepository<NoticeViewRecordEntity> {

    /**
     * 根据 userId 和 noticeId 查询
     *
     * @param userId   userId
     * @param noticeId noticeId
     * @return NoticeViewRecordEntity
     */
    NoticeViewRecordEntity getByUserIdAndNoticeId(Integer userId, Integer noticeId);

    /**
     * 根据 userId 查询
     *
     * @param userId userId
     * @return List<NoticeViewRecordEntity>
     */
    List<NoticeViewRecordEntity> getByUserId(Integer userId);
}