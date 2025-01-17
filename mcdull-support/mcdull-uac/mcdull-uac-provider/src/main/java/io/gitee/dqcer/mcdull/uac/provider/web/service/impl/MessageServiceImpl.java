package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.MessageAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MessageQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MessageEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.MessageTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MessageVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMessageRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Message Service
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class MessageServiceImpl
        extends BasicServiceImpl<IMessageRepository> implements IMessageService {

    @Resource
    private IAuditManager auditManager;

    @Override
    public Integer getUnreadCount(Integer userId) {
        return super.baseRepository.getUnreadCount(userId);
    }

    @Override
    public PagedVO<MessageVO> query(MessageQueryDTO dto) {
        List<MessageVO> voList = new ArrayList<>();
        Page<MessageEntity> entityPage = baseRepository.selectPage(dto);
        List<MessageEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            for (MessageEntity entity : recordList) {
                MessageVO vo = this.convertToVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateReadFlag(Integer id, Integer userId) {
        MessageEntity message = baseRepository.getById(id);
        if (ObjUtil.isNull(message)) {
            this.throwDataNotExistException(id);
        }
        if (message.getReadFlag()) {
            this.throwDataNeedRefreshException("id: {}", id);
        }
        baseRepository.updateReadFlag(id, userId);
        return true;
    }

    @Override
    public void insert(MessageTypeEnum typeEnum, Integer receiverUserId, String dataId,
                       String title, String content) {
        MessageEntity entity = new MessageEntity();
        entity.setMessageType(typeEnum.getCode());
        entity.setReceiverUserId(receiverUserId);
        entity.setDataId(dataId);
        entity.setTitle(title);
        entity.setContent(content);
        baseRepository.save(entity);
    }

    @Override
    public boolean getByUserId(Integer receiverUserId, String dataId) {
        MessageEntity entity = baseRepository.getByUserId(receiverUserId, dataId);
        return ObjectUtil.isNotNull(entity);
    }

    private Audit buildAuditLog(MessageEntity message) {
        MessageAudit audit = new MessageAudit();
        audit.setReadFlag(Boolean.toString(message.getReadFlag()));
        return audit;
    }

    private MessageVO convertToVO(MessageEntity entity) {
        MessageVO messageVO = new MessageVO();
        messageVO.setMessageId(entity.getId());
        messageVO.setMessageType(entity.getMessageType());
        messageVO.setReceiverUserId(entity.getReceiverUserId());
        messageVO.setDataId(entity.getDataId());
        messageVO.setTitle(entity.getTitle());
        messageVO.setContent(entity.getContent());
        messageVO.setReadFlag(entity.getReadFlag());
        messageVO.setReadTime(entity.getReadTime());
        messageVO.setCreatedTime(entity.getCreatedTime());
        return messageVO;
    }
}
