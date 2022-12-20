package com.dqcer.mcdull.mdc.client.dto;

import com.dqcer.framework.base.dto.DTO;
import com.dqcer.framework.base.util.ValidateUtil;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 邮件发送 请求类
 *
 * @author dqcer
 * @version 2022/11/01 22:11:30
 */
public class MailClientDTO implements DTO {

    private static final long serialVersionUID = -2080117833993961145L;

    /**
     * 编码
     */
    @Pattern(regexp = ValidateUtil.REGEXP_EMAIL)
    private String to;

    /**
     * 主题
     */
    @NotEmpty
    @Length(min = 1, max = 2048)
    private String subject;

    /**
     * 内容
     */
    @NotEmpty
    @Length(min = 1, max = 20480)
    private String content;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 抄送
     */
    private List<String> cc;

    @Override
    public String toString() {
        return "MailClientDTO{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", filePath='" + filePath + '\'' +
                ", cc=" + cc +
                '}';
    }

    public String getTo() {
        return to;
    }

    public MailClientDTO setTo(String to) {
        this.to = to;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public MailClientDTO setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailClientDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public MailClientDTO setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public List<String> getCc() {
        return cc;
    }

    public MailClientDTO setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }
}
