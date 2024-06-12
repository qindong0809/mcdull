package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;

/**
 * @author dqcer
 */
public interface IEmailSendHistoryService {

    boolean insert(List<String> sendToList, List<String> ccList, String title, String content);
}
