package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.NoticeVisibleRangeMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeVisibleRangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeVisibleRangeRepositoryImpl extends
        ServiceImpl<NoticeVisibleRangeMapper, NoticeVisibleRangeEntity> implements INoticeVisibleRangeRepository {

    @Override
    public List<NoticeVisibleRangeEntity> list(NoticeVisibleRangeEntity entity) {
        LambdaQueryChainWrapper<NoticeVisibleRangeEntity> query = this.lambdaQuery(entity);
        return query.list();
    }
}