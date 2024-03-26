package io.gitee.dqcer.mcdull.mdc.client.vo;

/**
 *
 *
 * @author dqcer
 * @since 2024/03/26
 */
public class EmailTemplateClientVO {

    private Integer id;

    private String code;

    private String name;

    private String title;

    private String content;

    private String remark;

    public Integer getId() {
        return id;
    }

    public EmailTemplateClientVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public EmailTemplateClientVO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public EmailTemplateClientVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EmailTemplateClientVO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EmailTemplateClientVO setContent(String content) {
        this.content = content;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public EmailTemplateClientVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
