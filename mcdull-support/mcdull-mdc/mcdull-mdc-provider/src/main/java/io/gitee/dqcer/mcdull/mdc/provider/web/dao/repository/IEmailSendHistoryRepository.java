package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.EmailSendHistoryDO;

import java.util.List;

/**
 * @author dqcer
 */
public interface IEmailSendHistoryRepository extends IService<EmailSendHistoryDO> {


    /**
     * 批量插入
     *
     * @param list 列表
     * @return boolean
     */
    boolean batchInsert(List<EmailSendHistoryDO> list);
}
