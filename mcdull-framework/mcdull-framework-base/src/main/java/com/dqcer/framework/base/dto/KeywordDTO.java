package com.dqcer.framework.base.dto;


import com.dqcer.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

/**
 * 关键字查询
 *
 * @author dqcer
 * @version 11:20 2020/3/16
 */
public abstract class KeywordDTO implements DTO {

    private static final long serialVersionUID = -8417050850699510442L;
    /**
     * 关键字 最大长度512
     */
    @Length(groups = {ValidGroup.Paged.class, ValidGroup.Keyword.class}, max = 512)
    private String keyword;

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
