package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EmailSendHistoryQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.EmailSendHistoryEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.EmailSendHistoryMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IEmailSendHistoryRepository;
import org.springframework.stereotype.Service;

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
    public Page<EmailSendHistoryEntity> selectPage(EmailSendHistoryQueryDTO queryDTO) {
        LambdaQueryWrapper<EmailSendHistoryEntity> query = Wrappers.lambdaQuery();
        String keyword = queryDTO.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i -> i.like(EmailSendHistoryEntity::getTitle, keyword)
                    .or().like(EmailSendHistoryEntity::getContent, keyword));
        }
        String sendTo = queryDTO.getSendTo();
        if (StrUtil.isNotBlank(sendTo)) {
            query.like(EmailSendHistoryEntity::getSentTo, sendTo);
        }
        query.orderByDesc(EmailSendHistoryEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()), query);
    }
}
