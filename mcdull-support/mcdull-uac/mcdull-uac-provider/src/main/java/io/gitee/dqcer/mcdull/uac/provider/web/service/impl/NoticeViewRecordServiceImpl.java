package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeViewRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeViewRecordRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeViewRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeViewRecordServiceImpl
        extends BasicServiceImpl<INoticeViewRecordRepository> implements INoticeViewRecordService {


    @Override
    public NoticeViewRecordEntity getByUserIdAndNoticeId(Long userId, Long noticeId) {
        return baseRepository.getByUserIdAndNoticeId(userId, noticeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(NoticeViewRecordEntity newEntity) {
        baseRepository.save(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(NoticeViewRecordEntity entity) {
        baseRepository.updateById(entity);
    }
}
