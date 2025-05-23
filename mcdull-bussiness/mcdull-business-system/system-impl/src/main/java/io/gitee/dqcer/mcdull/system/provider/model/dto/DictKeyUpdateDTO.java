package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

/**
 * 字典
 *
 * @author dqcer
 * @since 2024/04/28
 */
@Getter
@Setter
@ToString
public class DictKeyUpdateDTO extends DictKeyAddDTO {

    @Schema(description = "keyId")
    @NotNull(message = "keyId不能为空")
    private Integer dictKeyId;
}