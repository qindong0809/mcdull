package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.Map;


@Data
public class FormRecordAddDTO implements DTO {

    @NotNull
    private Integer formId;


    private Map<String, Object> formData;

}