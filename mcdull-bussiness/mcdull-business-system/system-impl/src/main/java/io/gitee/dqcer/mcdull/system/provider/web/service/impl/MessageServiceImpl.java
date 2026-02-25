package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.audit.MessageAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.MessageQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.MessageEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.MessageTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.MessageVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.MessageMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IMessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Message Service
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class MessageServiceImpl
        extends BasicCurdServiceImpl<MessageMapper, MessageEntity> implements IMessageService {

    @Resource
    private IAuditManager auditManager;

    @Override
    public Integer getUnreadCount(Integer userId) {
        LambdaQueryWrapper<MessageEntity> query = Wrappers.lambdaQuery();
        query.eq(MessageEntity::getReceiverUserId, userId).eq(MessageEntity::getReadFlag, false);
        return Convert.toInt(this.count(query));
    }

    @Override
    public PagedVO<MessageVO> query(MessageQueryDTO dto) {
        List<MessageVO> voList = new ArrayList<>();
        Page<MessageEntity> entityPage = this.selectPage(dto);
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
        MessageEntity message = super.mustGet(id);
        if (message.getReadFlag()) {
            LogicCheckUtil.throwDataNeedRefreshException("id: {}", id);
        }

        LambdaUpdateWrapper<MessageEntity> update = Wrappers.lambdaUpdate();
        update.set(MessageEntity::getReadFlag, true);
        update.eq(IdEntity::getId, id);
        return this.update(update);
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
        this.save(entity);
    }

    @Override
    public boolean getByUserId(Integer receiverUserId, String dataId) {
        LambdaQueryWrapper<MessageEntity> query = Wrappers.lambdaQuery();
        query.eq(MessageEntity::getReceiverUserId, receiverUserId);
        query.eq(MessageEntity::getDataId, dataId);
        List<MessageEntity> list = this.list(query);
        return CollUtil.isNotEmpty(list);
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

    public Page<MessageEntity> selectPage(MessageQueryDTO param) {
        LambdaQueryWrapper<MessageEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getSearchWord();
        if (ObjectUtil.isNotNull(keyword)) {
            lambda.and(i->i.like(MessageEntity::getTitle, keyword).or()
                .like(MessageEntity::getContent, keyword));
        }
        Boolean readFlag = param.getReadFlag();
        if (ObjectUtil.isNotNull(readFlag)) {
            lambda.eq(MessageEntity::getReadFlag, readFlag);
        }
        Date startDate = param.getStartDate();
        Date endDate = param.getEndDate();
        if (ObjectUtil.isNotNull(startDate) && ObjectUtil.isNotNull(endDate)) {
            lambda.between(MessageEntity::getCreatedTime, startDate, endDate);
        }
        lambda.eq(MessageEntity::getReceiverUserId, param.getReceiverUserId());
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }
}
