package io.gitee.dqcer.mcdull.framework.base.dto;


import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import org.hibernate.validator.constraints.Length;

/**
 * 关键字查询
 *
 * @author dqcer
 * @since 11:20 2020/3/16
 */
public class KeywordDTO implements DTO {

    private static final long serialVersionUID = 1L;
    /**
     * 关键字 最大长度512
     */
    @Length(max = 256)
    protected String keyword;

    @Override
    public String toString() {
        return "KeywordDTO{" +
                "keyword='" + keyword + '\'' +
                '}';
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
