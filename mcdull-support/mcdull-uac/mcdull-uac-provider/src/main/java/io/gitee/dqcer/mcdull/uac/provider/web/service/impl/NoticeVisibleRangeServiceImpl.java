package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeVisibleRangeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeVisibleRangeService;
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
        extends BasicServiceImpl<INoticeVisibleRangeRepository> implements INoticeVisibleRangeService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<NoticeVisibleRangeEntity> list) {
        baseRepository.saveBatch(list, list.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NoticeVisibleRangeEntity> getListByNoticeId(Integer noticeId) {
        NoticeVisibleRangeEntity entity = new NoticeVisibleRangeEntity();
        entity.setNoticeId(Convert.toInt(noticeId));
        return baseRepository.list(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<NoticeVisibleRangeEntity> insertList,
                       List<NoticeVisibleRangeEntity> updateList,
                       List<NoticeVisibleRangeEntity> removeList) {
        if (CollUtil.isNotEmpty(insertList)) {
            baseRepository.saveBatch(insertList, insertList.size());
        }
        if (CollUtil.isNotEmpty(updateList)) {
            baseRepository.updateBatchById(updateList, updateList.size());
        }
        if (CollUtil.isNotEmpty(removeList)) {
            baseRepository.removeByIds(removeList);
        }
    }
}
