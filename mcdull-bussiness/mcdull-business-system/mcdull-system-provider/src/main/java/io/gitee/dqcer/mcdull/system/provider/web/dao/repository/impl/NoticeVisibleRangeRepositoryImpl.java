package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeVisibleRangeEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.NoticeVisibleRangeMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.INoticeVisibleRangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Notice visible range repository impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class NoticeVisibleRangeRepositoryImpl extends
        CrudRepository<NoticeVisibleRangeMapper, NoticeVisibleRangeEntity> implements INoticeVisibleRangeRepository {

    @Override
    public List<NoticeVisibleRangeEntity> list(NoticeVisibleRangeEntity entity) {
        LambdaQueryChainWrapper<NoticeVisibleRangeEntity> query = this.lambdaQuery(entity);
        return query.list();
    }
}