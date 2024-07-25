package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.EmailSendHistoryEntity;

import java.util.List;

/**
 * Email send history repository
 *
 * @author dqcer
 * @since 2024/7/25 11:09
 */

public interface IEmailSendHistoryRepository extends IService<EmailSendHistoryEntity> {


    /**
     * 批量插入
     *
     * @param list 列表
     * @return boolean
     */
    boolean batchInsert(List<EmailSendHistoryEntity> list);
}
