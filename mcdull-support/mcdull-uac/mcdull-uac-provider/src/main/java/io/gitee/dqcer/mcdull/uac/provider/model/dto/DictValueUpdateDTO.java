package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


/**
 * dict
 *
 * @author dqcer
 * @since 2024/04/28
 */
@Getter
@Setter
@ToString
public class DictValueUpdateDTO extends DictValueAddDTO {

    @Schema(description = "valueId")
    @NotNull(message = "valueId不能为空")
    private Integer dictValueId;
}