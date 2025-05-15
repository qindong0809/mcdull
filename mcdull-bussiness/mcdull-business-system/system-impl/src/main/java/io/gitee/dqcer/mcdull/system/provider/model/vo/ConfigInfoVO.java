package io.gitee.dqcer.mcdull.system.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
* 系统配置 返回客户端值
*
* @author dqcer
* @since 2024-04-29
*/
@EqualsAndHashCode(callSuper = false)
@Data
public class ConfigInfoVO implements VO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer configId;

    @Schema(description = "参数名字")
    private String configName;

    @Schema(description = "参数key")
    private String configKey;

    @Schema(description = "参数值")
    private String configValue;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @Schema(description = "上次修改时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;


}