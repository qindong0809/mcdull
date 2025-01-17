package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * form record dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FormRecordUpdateDTO extends FormRecordAddDTO {

    @NotNull
    private Integer recordId;
}