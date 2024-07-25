package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeViewRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeViewRecordRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeViewRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Notice View Record Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class NoticeViewRecordServiceImpl
        extends BasicServiceImpl<INoticeViewRecordRepository> implements INoticeViewRecordService {


    @Override
    public NoticeViewRecordEntity getByUserIdAndNoticeId(Integer userId, Integer noticeId) {
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

    @Override
    public List<Integer> getByUserId(Integer userId) {
        List<NoticeViewRecordEntity> list = baseRepository.getByUserId(userId);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(NoticeViewRecordEntity::getNoticeId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
