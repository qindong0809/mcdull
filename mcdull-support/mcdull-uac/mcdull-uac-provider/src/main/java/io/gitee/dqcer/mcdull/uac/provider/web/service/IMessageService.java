package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MessageQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.MessageTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MessageVO;

/**
 * Message Service
 *
 * @author dqcer
 * @since 2024/7/25 9:16
 */

public interface IMessageService {

    Integer getUnreadCount(Integer userId);

    PagedVO<MessageVO> query(MessageQueryDTO queryForm);

    boolean updateReadFlag(Integer id, Integer integer);

    void insert(MessageTypeEnum typeEnum, Integer receiverUserId, String dataId,
                String title, String content);

    boolean getByUserId(Integer receiverUserId, String dataId);
}
