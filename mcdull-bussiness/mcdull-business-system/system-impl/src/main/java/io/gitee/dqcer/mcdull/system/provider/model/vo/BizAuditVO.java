package io.gitee.dqcer.mcdull.system.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.business.common.audit.AuditUtil;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 业务审计 VO
 *
 * @author dqcer
 * @since 2024/10/29
 */
@Data
public class BizAuditVO implements VO {

    private Integer id;

    @Schema(description = "业务类型码")
    private String bizTypeCode;

    @Schema(description = "业务类型名称")
    private String bizTypeName;

    @Schema(description = "业务全称")
    private String bizTypeRootName;

    @Schema(description = "业务索引")
    private String bizIndex;

    @Schema(description = "业务动作名称")
    private String operationName;

    @Schema(description = "业务动作")
    private Integer operation;

    @Schema(description = "业务内容")
    private String comment;

    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "操作人名称")
    private String operatorName;

    @Schema(description = "操作时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date operationTime;

    @Schema(description = "差异列表")
    private List<AuditUtil.FieldDiff> diffList;
}