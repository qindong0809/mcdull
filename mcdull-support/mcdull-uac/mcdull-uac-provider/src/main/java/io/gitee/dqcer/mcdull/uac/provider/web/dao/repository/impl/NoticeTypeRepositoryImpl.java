package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeTypeEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.NoticeTypeMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeTypeRepository;
import org.springframework.stereotype.Service;

/**
* 系统配置 数据库操作封装实现层
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeTypeRepositoryImpl
        extends ServiceImpl<NoticeTypeMapper, NoticeTypeEntity>  implements INoticeTypeRepository {



}