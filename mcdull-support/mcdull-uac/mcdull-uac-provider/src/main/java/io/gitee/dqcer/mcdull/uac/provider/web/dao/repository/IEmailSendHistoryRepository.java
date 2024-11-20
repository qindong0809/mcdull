package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EmailSendHistoryQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.EmailSendHistoryEntity;

/**
 * Email send history repository
 *
 * @author dqcer
 * @since 2024/7/25 11:09
 */

public interface IEmailSendHistoryRepository extends IRepository<EmailSendHistoryEntity> {

    Page<EmailSendHistoryEntity> selectPage(EmailSendHistoryQueryDTO queryDTO);
}
