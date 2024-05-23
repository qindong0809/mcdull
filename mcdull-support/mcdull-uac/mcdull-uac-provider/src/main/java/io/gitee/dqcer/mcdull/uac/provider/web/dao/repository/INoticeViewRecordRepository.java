package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeViewRecordEntity;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeViewRecordRepository extends IService<NoticeViewRecordEntity> {

    NoticeViewRecordEntity getByUserIdAndNoticeId(Long userId, Long noticeId);
}