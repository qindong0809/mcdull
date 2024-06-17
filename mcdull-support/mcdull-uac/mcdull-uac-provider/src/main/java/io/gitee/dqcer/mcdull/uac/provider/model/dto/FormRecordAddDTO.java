package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;


@Data
public class FormRecordAddDTO implements DTO {

    @NotNull
    private Integer formId;


    private Map<String, String> formData;

}