package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.EmailSendHistoryEntity;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.EmailSendHistoryMapper;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IEmailSendHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dqcer
 */
@Service
public class EmailSendHistoryRepositoryImpl extends ServiceImpl<EmailSendHistoryMapper, EmailSendHistoryEntity>
        implements IEmailSendHistoryRepository {

    @Override
    public boolean batchInsert(List<EmailSendHistoryEntity> list) {
        return this.executeBatch(list, 10, (session, entity) -> {
            EmailSendHistoryMapper mapper = session.getMapper(EmailSendHistoryMapper.class);
            mapper.insert(entity);
        });
    }
}
