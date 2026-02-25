package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeVisibleRangeEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.NoticeVisibleRangeMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.INoticeVisibleRangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Notice visible range service impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class NoticeVisibleRangeServiceImpl
        extends BasicCurdServiceImpl<NoticeVisibleRangeMapper, NoticeVisibleRangeEntity> implements INoticeVisibleRangeService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<NoticeVisibleRangeEntity> list) {
        super.saveBatch(list, list.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NoticeVisibleRangeEntity> getListByNoticeId(Integer noticeId) {
        NoticeVisibleRangeEntity entity = new NoticeVisibleRangeEntity();
        entity.setNoticeId(Convert.toInt(noticeId));
        return this.list(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<NoticeVisibleRangeEntity> insertList,
                       List<NoticeVisibleRangeEntity> updateList,
                       List<NoticeVisibleRangeEntity> removeList) {
        if (CollUtil.isNotEmpty(insertList)) {
            super.saveBatch(insertList, insertList.size());
        }
        if (CollUtil.isNotEmpty(updateList)) {
            super.updateBatchById(updateList, updateList.size());
        }
        if (CollUtil.isNotEmpty(removeList)) {
            super.removeByIds(removeList);
        }
    }

    public List<NoticeVisibleRangeEntity> list(NoticeVisibleRangeEntity entity) {
        LambdaQueryChainWrapper<NoticeVisibleRangeEntity> query = this.lambdaQuery(entity);
        return query.list();
    }
}
