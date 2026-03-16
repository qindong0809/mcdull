package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeViewRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.NoticeViewRecordMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.INoticeViewRecordService;
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
        extends BasicCurdServiceImpl<NoticeViewRecordMapper, NoticeViewRecordEntity> implements INoticeViewRecordService {


    @Override
    public NoticeViewRecordEntity getByUserIdAndNoticeId(Integer userId, Integer noticeId) {
        LambdaQueryWrapper<NoticeViewRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(NoticeViewRecordEntity::getNoticeId, noticeId);
        query.eq(NoticeViewRecordEntity::getUserId, userId);
        return baseMapper.selectOne(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(NoticeViewRecordEntity entity) {
        super.updateById(entity);
    }

    @Override
    public List<Integer> getByUserId(Integer userId) {
        LambdaQueryWrapper<NoticeViewRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(NoticeViewRecordEntity::getUserId, userId);
        List<NoticeViewRecordEntity> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(NoticeViewRecordEntity::getNoticeId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
