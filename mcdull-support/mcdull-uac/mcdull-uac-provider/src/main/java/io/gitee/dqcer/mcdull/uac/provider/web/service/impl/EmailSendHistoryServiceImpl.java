package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.EmailSendHistoryEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IEmailSendHistoryRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailSendHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Email send history service impl
 *
 * @author dqcer
 * @since 2024/03/25
 */
@Service
public class EmailSendHistoryServiceImpl
        extends BasicServiceImpl<IEmailSendHistoryRepository> implements IEmailSendHistoryService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(List<String> sendToList, List<String> ccList, String title, String content) {
        if (CollUtil.isEmpty(sendToList) || StrUtil.isBlank(title) || StrUtil.isBlank(content)) {
            LogHelp.error(log, "insert email send history fail, sendToList: {}, title: {}, content: {}", sendToList, title, content);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        EmailSendHistoryEntity sendHistory = new EmailSendHistoryEntity();
        sendHistory.setSentTo(String.join(StrUtil.COMMA, sendToList));
        if (CollUtil.isNotEmpty(ccList)) {
            sendHistory.setCc(String.join(StrUtil.COMMA, ccList));
        }
        sendHistory.setTitle(title);
        sendHistory.setContent(content);
        return baseRepository.batchInsert(ListUtil.of(sendHistory));
    }

}
