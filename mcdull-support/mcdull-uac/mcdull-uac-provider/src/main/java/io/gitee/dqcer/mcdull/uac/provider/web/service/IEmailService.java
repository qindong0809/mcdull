package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.enums.EmailTypeEnum;

/**
 * Email Service
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IEmailService {

    /**
     * 发送电子邮件
     *
     * @param emailTypeEnum email 类型 enum
     * @param sendTo        发送到
     * @param subject       主题
     * @param text          文本
     * @return boolean
     */
    boolean sendEmail(EmailTypeEnum emailTypeEnum, String sendTo, String subject, String text);


    /**
     * 发送电子邮件 html
     *
     * @param emailTpeEmail emailTpeEmail
     * @param sendTo        发送到
     * @param subject       主题
     * @param text          文本
     */
    void sendEmailHtml(EmailTypeEnum emailTpeEmail, String sendTo, String subject, String text);

    /**
     * 用字节发送电子邮件
     *
     * @param bytes    字节
     * @param fileName 文件名
     * @param sendTo   发送到
     * @param subject  主题
     * @param text     文本
     * @return boolean
     */
    boolean sendEmailWithBytes(byte[] bytes, String fileName, String sendTo, String subject, String text);
}
