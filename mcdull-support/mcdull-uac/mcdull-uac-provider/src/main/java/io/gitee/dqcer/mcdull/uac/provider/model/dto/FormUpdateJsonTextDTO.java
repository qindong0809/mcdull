package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class FormUpdateJsonTextDTO implements DTO {

    @NotNull
    private Integer id;

    @NotBlank
    private String jsonText;

}