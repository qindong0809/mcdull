package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.SysEmailSendHistoryDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.SysEmailSendHistoryDAO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.ISysEmailSendHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dqcer
 */
@Service
public class SysEmailSendHistoryRepository extends ServiceImpl<SysEmailSendHistoryDAO, SysEmailSendHistoryDO>
        implements ISysEmailSendHistoryRepository {

    @Override
    public boolean batchInsert(List<SysEmailSendHistoryDO> list) {
        return this.executeBatch(list, 10, (session, entity) -> {
            SysEmailSendHistoryDAO mapper = session.getMapper(SysEmailSendHistoryDAO.class);
            mapper.insert(entity);
        });
    }
}
