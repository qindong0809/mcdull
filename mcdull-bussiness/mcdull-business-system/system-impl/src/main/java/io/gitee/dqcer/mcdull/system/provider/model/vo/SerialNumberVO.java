package io.gitee.dqcer.mcdull.system.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author dqcer
 * @since 2024/05/27
 */
@Data
public class SerialNumberVO implements VO {

    @Schema(description = "流水号id")
    private Integer serialNumberId;

    @Schema(description = "业务名称")
    private String businessName;

    @Schema(description = "业务类型")
    private Integer businessType;

    @Schema(description = "流水号格式")
    private String format;

    @Schema(description = "流水号规则类型")
    private String ruleType;

    @Schema(description = "流水号初始值")
    private Integer initNumber;

    @Schema(description = "流水号步长")
    private Integer stepRandomRange;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "最后流水号")
    private Integer lastNumber;

    @Schema(description = "最后流水号时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date lastTime;
}
