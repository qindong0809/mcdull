package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
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
public class DictValueQueryDTO extends PagedDTO {

    @Schema(description = "dictKeyId")
    @NotNull(message = "dictKeyId不能为空")
    private Integer dictKeyId;

    @Schema(description = "搜索词")
    private String searchWord;

    @Schema(description = "删除标识",hidden = true)
    private Boolean deletedFlag;
}