package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class FormAddDTO implements DTO {


    @NotBlank
    private String name;

    private String remark;

}