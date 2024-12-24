package io.gitee.dqcer.mcdull.uac.provider.model.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * FormVO
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Data
public class FormVO implements VO {

    @Schema(description = "表单id")
    private Integer id;

    @Schema(description = "表单名称")
    private String name;

    @Schema(description = "表单JSON")
    private String jsonText;

    @Schema(description = "是否发布")
    private Boolean publish;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @Schema(description = "更新时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;

    @Schema(description = "数据条数")
    private Integer dataNumber;

}
