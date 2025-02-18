package io.gitee.dqcer.mcdull.mdc.client.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.LanguageEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;

import jakarta.validation.constraints.NotEmpty;

/**
 * sys dict dto
 *
 * @author dqcer
 * @since 2022/11/01 22:11:30
 */
public class DictClientDTO implements DTO {

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

    @Override
    public String toString() {
        return "DictClientDTO{" +
                "code='" + code + '\'' +
                ", selectType='" + selectType + '\'' +
                ", language='" + language + '\'' +
                '}';
    }

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
