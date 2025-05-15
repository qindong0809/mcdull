package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeTypeEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.NoticeTypeMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.INoticeTypeRepository;
import org.springframework.stereotype.Service;

/**
* Notice Type Repository
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeTypeRepositoryImpl
        extends CrudRepository<NoticeTypeMapper, NoticeTypeEntity> implements INoticeTypeRepository {



}