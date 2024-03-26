package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IEmailSendHistoryService {
    @Transactional(rollbackFor = Exception.class)
    boolean insert(List<String> sendToList, List<String> ccList, String title, String content);
}
