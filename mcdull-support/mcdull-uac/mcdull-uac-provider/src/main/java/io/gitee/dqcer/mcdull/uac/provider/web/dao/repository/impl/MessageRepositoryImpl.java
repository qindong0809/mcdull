package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MessageQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MessageEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.MessageMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMessageRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Message 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class MessageRepositoryImpl extends
        CrudRepository<MessageMapper, MessageEntity> implements IMessageRepository {


    @Override
    public Integer getUnreadCount(Integer userId) {
        LambdaQueryWrapper<MessageEntity> query = Wrappers.lambdaQuery();
        query.eq(MessageEntity::getReceiverUserId, userId).eq(MessageEntity::getReadFlag, false);
        return Convert.toInt(this.count(query));
    }

    @Override
    public Page<MessageEntity> selectPage(MessageQueryDTO param) {
        LambdaQueryWrapper<MessageEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getSearchWord();
        if (ObjUtil.isNotNull(keyword)) {
            lambda.and(i->i.like(MessageEntity::getTitle, keyword).or()
                    .like(MessageEntity::getContent, keyword));
        }
        Boolean readFlag = param.getReadFlag();
        if (ObjUtil.isNotNull(readFlag)) {
            lambda.eq(MessageEntity::getReadFlag, readFlag);
        }
        Date startDate = param.getStartDate();
        Date endDate = param.getEndDate();
        if (ObjUtil.isNotNull(startDate) && ObjUtil.isNotNull(endDate)) {
            lambda.between(MessageEntity::getCreatedTime, startDate, endDate);
        }
        lambda.eq(MessageEntity::getReceiverUserId, param.getReceiverUserId());
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public boolean updateReadFlag(Integer id, Integer userId) {
        LambdaUpdateWrapper<MessageEntity> update = Wrappers.lambdaUpdate();
        update.set(MessageEntity::getReadFlag, true);
        update.eq(IdEntity::getId, id);
        return this.update(update);
    }

    @Override
    public MessageEntity getByUserId(Integer receiverUserId, String dataId) {
        LambdaQueryWrapper<MessageEntity> query = Wrappers.lambdaQuery();
        query.eq(MessageEntity::getReceiverUserId, receiverUserId);
        query.eq(MessageEntity::getDataId, dataId);
        List<MessageEntity> list = this.list(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

}
