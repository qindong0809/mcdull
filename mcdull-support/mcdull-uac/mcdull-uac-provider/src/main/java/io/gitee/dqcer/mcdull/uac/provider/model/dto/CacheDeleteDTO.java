package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 缓存删除参数
 *
 * @author dqcer
 * @since 2024/06/18
 */

@Data
public class CacheDeleteDTO implements DTO {

    @NotNull
    @Schema(description = "caffeineCacheFlag")
    private Boolean caffeineCacheFlag;

    @NotBlank
    @Schema(description = "key")
    private String key;


}