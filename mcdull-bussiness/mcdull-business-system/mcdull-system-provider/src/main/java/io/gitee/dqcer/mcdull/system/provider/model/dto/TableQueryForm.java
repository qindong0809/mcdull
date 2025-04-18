package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 查询表数据
 *
 */
@Data
public class TableQueryForm extends PagedDTO {

    @Schema(description = "表名关键字")
    private String tableNameKeywords;

}
