package com.dqcer.framework.base;


import org.hibernate.validator.constraints.Length;

/**
 * 关键字查询
 *
 * @author dqcer
 * @date 11:20 2020/3/16
 */
public abstract class KeywordDTO extends DTO {

    /**
     * 关键字 最大长度64
     */
    @Length(groups = {ValidGroup.Paged.class}, max = 512)
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
