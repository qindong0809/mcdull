package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;

/**
 * Email Send History Service
 *
 * @author dqcer
 * @since 2024/7/25 9:21
 */

public interface IEmailSendHistoryService {

    boolean insert(List<String> sendToList, List<String> ccList, String title, String content);
}
