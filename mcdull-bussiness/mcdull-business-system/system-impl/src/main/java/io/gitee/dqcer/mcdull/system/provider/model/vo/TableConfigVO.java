
package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 表的配置信息
 *
 */

@Data
public class TableConfigVO {

    @Schema(description = "基础命名信息")
    private CodeBasic basic;

    @Schema(description = "字段列")
    private List<CodeField> fields;

    @Schema(description = "增加、修改 信息")
    private CodeInsertAndUpdate insertAndUpdate;

    @Schema(description = "删除 信息")
    private CodeDelete deleteInfo;

    @Schema(description = "查询字段")
    private List<CodeQueryField> queryFields;

    @Schema(description = "列表字段")
    private List<CodeTableField> tableFields;
}
