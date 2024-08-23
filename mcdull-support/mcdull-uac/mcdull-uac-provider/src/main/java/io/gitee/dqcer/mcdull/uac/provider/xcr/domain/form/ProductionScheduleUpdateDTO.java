package io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 生产进度表 更新表单
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */

@Data
public class ProductionScheduleUpdateDTO implements DTO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键 不能为空")
    private Integer id;

    @Schema(description = "客户", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "客户 不能为空")
    private String customer;

    @Schema(description = "联系方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "联系方式 不能为空")
    private String contactInfo;

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "地址 不能为空")
    private String address;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "产品名称 不能为空")
    private String productName;

    @Schema(description = "测量日期")
    private Date measurementDate;

    @Schema(description = "测量人")
    private Integer measurementBy;

    @Schema(description = "制图完成日期")
    private Date drawingDate;

    @Schema(description = "制图人")
    private Integer drawingBy;

    @Schema(description = "客户确认日期")
    private Date customerConfirmationDate;

    @Schema(description = "拆单日期")
    private Date orderBreakingDate;

    @Schema(description = "拆单人")
    private Integer orderBreakingBy;

    @Schema(description = "下生产单日期")
    private Date productionOrderDate;

    @Schema(description = "工序日期")
    private Date processDate;

    @Schema(description = "工序描述")
    private String processDescription;

    @Schema(description = "出货日期")
    private Date deliveryDate;

    @Schema(description = "出货人")
    private Integer deliveryBy;

    @Schema(description = "安装日期")
    private Date installationDate;

    @Schema(description = "安装人")
    private Integer installationBy;

    @Schema(description = "生产周期倒计时")
    private Integer productionCycleRemaining;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "负责人 不能为空")
    private Integer owner;

    @Schema(description = "状态（true/已失活 false/未失活）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态（true/已失活 false/未失活） 不能为空")
    private Boolean inactive;

}