package com.dqcer.mcdull.mdc.api.dto;

import com.dqcer.framework.base.DTO;
import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.annotation.EnumsStrValid;
import com.dqcer.framework.base.enums.LanguageEnum;

import javax.validation.constraints.NotEmpty;

/**
 * sys dict dto
 *
 * @author dqcer
 * @version 2022/11/01 22:11:30
 */
public class DictFeignDTO extends DTO {

    /**
     * 编码
     */
    @NotEmpty(groups = { ValidGroup.One.class })
    private String code;

    /**
     * 类别
     */
    @NotEmpty(groups = { ValidGroup.One.class, ValidGroup.List.class })
    private String selectType;

    /**
     * language
     */
    @EnumsStrValid(value = LanguageEnum.class, groups = { ValidGroup.One.class, ValidGroup.List.class })
    private String language;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
