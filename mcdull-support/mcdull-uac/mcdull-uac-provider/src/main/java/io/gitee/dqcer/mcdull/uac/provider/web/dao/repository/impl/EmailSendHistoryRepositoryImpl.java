package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.EmailSendHistoryEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.EmailSendHistoryMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IEmailSendHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Email send history repository impl
 *
 * @author dqcer
 * @since 2024/7/25 13:17
 */

@Service
public class EmailSendHistoryRepositoryImpl
        extends CrudRepository<EmailSendHistoryMapper, EmailSendHistoryEntity> implements IEmailSendHistoryRepository {

    @Override
    public boolean batchInsert(List<EmailSendHistoryEntity> list) {
        return this.executeBatch(list, 10, (session, entity) -> {
            EmailSendHistoryMapper mapper = session.getMapper(EmailSendHistoryMapper.class);
            mapper.insert(entity);
        });
    }
}
