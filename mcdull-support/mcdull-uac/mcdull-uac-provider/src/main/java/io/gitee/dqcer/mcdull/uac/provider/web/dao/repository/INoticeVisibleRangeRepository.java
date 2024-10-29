package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;

import java.util.List;

/**
 * Notice Visible Range Repository
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeVisibleRangeRepository extends IRepository<NoticeVisibleRangeEntity> {

    /**
     * list
     *
     * @param entity entity
     * @return {@link List}<{@link NoticeVisibleRangeEntity}>
     */
    List<NoticeVisibleRangeEntity> list(NoticeVisibleRangeEntity entity);
}