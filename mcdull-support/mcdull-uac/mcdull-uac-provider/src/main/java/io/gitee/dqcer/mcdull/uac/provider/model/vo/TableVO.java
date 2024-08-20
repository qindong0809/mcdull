
package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

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
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @Schema(description = "更新时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;

    @Schema(description = "配置时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date configTime;

}
