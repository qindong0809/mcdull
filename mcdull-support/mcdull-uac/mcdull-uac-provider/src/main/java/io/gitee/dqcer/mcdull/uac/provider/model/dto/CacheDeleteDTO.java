package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 缓存删除参数
 *
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