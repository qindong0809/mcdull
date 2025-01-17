package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import jakarta.validation.constraints.NotNull;


/**
 * 类型配置发布
 *
 * @author dqcer
 * @since 2024/06/18
 */
@Data
public class FormConfigReadyDTO implements DTO {

    @NotNull
    private Integer formId;

}