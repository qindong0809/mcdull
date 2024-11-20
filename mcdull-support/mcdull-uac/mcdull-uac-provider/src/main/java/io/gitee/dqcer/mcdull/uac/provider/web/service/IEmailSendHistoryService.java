package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EmailSendHistoryQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.EmailTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EmailSendHistoryVO;

import java.util.List;

/**
 * Email Send History Service
 *
 * @author dqcer
 * @since 2024/7/25 9:21
 */

public interface IEmailSendHistoryService {

    /**
     * 插入
     *
     * @param typeEnum   类型 enum
     * @param sendToList 发送到列表
     * @param ccList     抄送列表
     * @param title      标题
     * @param content    内容
     */
    void insert(EmailTypeEnum typeEnum, List<String> sendToList, List<String> ccList, String title, String content);

    /**
     * 插入
     *
     * @param typeEnum 类型 enum
     * @param sendTo   发送到
     * @param title    标题
     * @param content  内容
     */
    void insert(EmailTypeEnum typeEnum, String sendTo, String title, String content);

    /**
     * Query 页面
     *
     * @param queryDTO 查询 DTO
     * @return {@link PagedVO }<{@link EmailSendHistoryVO }>
     */
    PagedVO<EmailSendHistoryVO> queryPage(EmailSendHistoryQueryDTO queryDTO);
}
