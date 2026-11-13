package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Data
public class FormUpdateJsonTextDTO implements DTO {

    @NotNull
    private Integer id;

    @NotBlank
    private String jsonText;

}