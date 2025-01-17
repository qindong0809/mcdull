package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class FormUpdateDTO extends FormAddDTO {

    @NotNull
    private Integer id;


}