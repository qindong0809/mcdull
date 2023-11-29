package io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc;

/**
 * @author dqcer
 */
public interface IMailManager {

    /**
     * 发送电子邮件
     *
     * @param sendTo  发送到
     * @param subject 主题
     * @param text    文本
     * @return boolean
     */
    boolean sendEmail(String sendTo, String subject, String text);
}
