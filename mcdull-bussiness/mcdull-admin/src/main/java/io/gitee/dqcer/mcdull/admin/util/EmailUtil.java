package io.gitee.dqcer.mcdull.admin.util;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 电子邮件
 *
 * @author dqcer
 * @since 2023/02/07
 */
public class EmailUtil {

    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);

    public static void main(String[] args) {
        generateEmlFile("ddqcer@sina.com", "ddd@qq.com,cvdv@qq.com","cc@qq.com,bb@edetek.com", "subect", "body", "D:\\1.eml");
    }

    /**
     * 生成eml文件
     *
     * @param from        发送者
     * @param to          接收者
     * @param cc          抄送者
     * @param subject     主题
     * @param body        内容
     * @param emlFilePath eml文件路径
     */
    public static void generateEmlFile(String from, String to, String cc, String subject, String body, String emlFilePath) {
        try {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setText(body);
            msg.saveChanges();
            msg.writeTo(Files.newOutputStream(Paths.get(emlFilePath)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void send(String host, String username, String pass, Integer port, List<String> toList,
                            List<String> ccList, String subject, String context, boolean isHtml) {
        MailAccount account = new MailAccount();
        account.setHost(host);
        account.setUser(username);
        account.setPass(pass);
        account.setPort(port);
        MailUtil.send(account, toList, ccList, null, subject, context, isHtml);
    }

}
