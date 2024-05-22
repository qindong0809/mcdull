package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeVisibleRangeRepository extends IService<NoticeVisibleRangeEntity> {

    List<NoticeVisibleRangeEntity> list(NoticeVisibleRangeEntity entity);
}