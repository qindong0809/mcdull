package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 字典
 *
 * @author dqcer
 * @serial 2024/04/28
 */
@Getter
@Setter
@ToString
public class DictKeyQueryDTO extends PagedDTO {

    @Schema(description = "搜索词")
    private String searchWord;

    @Schema(description = "删除标识",hidden = true)
    private Boolean deletedFlag;
}