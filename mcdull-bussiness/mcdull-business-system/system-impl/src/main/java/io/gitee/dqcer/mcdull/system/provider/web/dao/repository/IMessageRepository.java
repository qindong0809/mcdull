package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MessageQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.MessageEntity;


/**
 * Message repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IMessageRepository extends IRepository<MessageEntity> {

    Integer getUnreadCount(Integer userId);

    Page<MessageEntity> selectPage(MessageQueryDTO dto);

    boolean updateReadFlag(Integer id, Integer userId);

    MessageEntity getByUserId(Integer receiverUserId, String dataId);
}