package io.gitee.dqcer.mcdull.uac.provider.web.service;

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
     * @param sendTo  发送到
     * @param subject 主题
     * @param text    文本
     * @return boolean
     */
    boolean sendEmail(String sendTo, String subject, String text);


    /**
     * 发送电子邮件 html
     *
     * @param sendTo  发送到
     * @param subject 主题
     * @param text    文本
     * @return boolean
     */
    boolean sendEmailHtml(String sendTo, String subject, String text);

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
