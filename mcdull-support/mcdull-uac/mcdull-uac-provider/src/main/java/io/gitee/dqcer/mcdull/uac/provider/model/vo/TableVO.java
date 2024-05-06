
package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表信息
 *
 */

@Data
public class TableVO {

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表备注")
    private String tableComment;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "配置时间")
    private LocalDateTime configTime;

}
