package io.gitee.dqcer.mcdull.mdc.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.EmailSendHistoryDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IEmailSendHistoryRepository;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.IEmailSendHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 电子邮件发送历史记录服务
 *
 * @author dqcer
 * @since 2024/03/25
 */
@Service
public class EmailSendHistoryServiceImpl extends BasicServiceImpl<IEmailSendHistoryRepository>
        implements IEmailSendHistoryService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(List<String> sendToList, List<String> ccList, String title, String content) {
        if (CollUtil.isEmpty(sendToList) || StrUtil.isBlank(title) || StrUtil.isBlank(content)) {
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        EmailSendHistoryDO sendHistory = new EmailSendHistoryDO();
        sendHistory.setSentTo(String.join(StrUtil.COMMA, sendToList));
        if (CollUtil.isNotEmpty(ccList)) {
            sendHistory.setCc(String.join(StrUtil.COMMA, ccList));
        }
        sendHistory.setTitle(title);
        sendHistory.setContent(content);
        return baseRepository.batchInsert(ListUtil.of(sendHistory));
    }

}
