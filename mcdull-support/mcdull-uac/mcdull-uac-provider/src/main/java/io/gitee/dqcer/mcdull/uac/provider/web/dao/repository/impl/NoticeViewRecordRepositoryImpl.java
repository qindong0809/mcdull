package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeViewRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.NoticeViewRecordMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeViewRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeViewRecordRepositoryImpl extends
        ServiceImpl<NoticeViewRecordMapper, NoticeViewRecordEntity> implements INoticeViewRecordRepository {


    @Override
    public NoticeViewRecordEntity getByUserIdAndNoticeId(Long userId, Long noticeId) {
        LambdaQueryWrapper<NoticeViewRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(NoticeViewRecordEntity::getNoticeId, noticeId);
        query.eq(NoticeViewRecordEntity::getUserId, userId);
        return baseMapper.selectOne(query);
    }

    @Override
    public List<NoticeViewRecordEntity> getByUserId(Long userId) {
        LambdaQueryWrapper<NoticeViewRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(NoticeViewRecordEntity::getUserId, userId);
        return baseMapper.selectList(query);
    }
}