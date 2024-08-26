package io.gitee.dqcer.mcdull.uac.provider.xcr.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 生产进度表 列表VO
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */

@Data
public class ProductionScheduleVO implements VO {

    private Integer id;

    @Schema(description = "客户")
    private String customer;

    @Schema(description = "联系方式")
    private String contactInfo;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "地址 + 客户")
    private String addressCustomer;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "测量日期")
    private Date measurementDate;

    @Schema(description = "测量人")
    private Integer measurementBy;

    private String measurement;

    @Schema(description = "测量人名称")
    private String measurementByName;

    @Schema(description = "制图完成日期")
    private Date drawingDate;

    @Schema(description = "制图人")
    private Integer drawingBy;

    private String drawingByName;

    private String drawing;
    @Schema(description = "客户确认日期")
    private Date customerConfirmationDate;

    private String customerConfirmation;

    @Schema(description = "拆单日期")
    private Date orderBreakingDate;

    @Schema(description = "拆单人")
    private Integer orderBreakingBy;

    private String orderBreakingByName;

    private String orderBreaking;
    @Schema(description = "下生产单日期")
    private Date productionOrderDate;

    private String productionOrder;

    @Schema(description = "工序日期")
    private Date processDate;

    @Schema(description = "工序描述")
    private String processDescription;

    private String process;

    @Schema(description = "出货日期")
    private Date deliveryDate;

    @Schema(description = "出货人")
    private Integer deliveryBy;

    private String deliveryByName;

    private String delivery;
    @Schema(description = "安装日期")
    private Date installationDate;

    @Schema(description = "安装人")
    private Integer installationBy;

    private String installationByName;

    private String installation;
    @Schema(description = "生产周期倒计时")
    private Integer productionCycleRemaining;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "负责人")
    private Integer owner;

    private String ownerName;

    @Schema(description = "是否可编辑")
    private Boolean editAble;

    @Schema(description = "创建人")
    private Integer createdBy;

    private String createdByName;

    @DynamicDateFormat(appendTimezoneStyle = false)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "创建时间")
    private Date createdTime;

    @Schema(description = "更新人")
    private Integer updatedBy;

    private String updatedByName;

    @DynamicDateFormat(appendTimezoneStyle = false)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "更新时间")
    private Date updatedTime;

    @Schema(description = "状态（true/已失活 false/未失活）")
    private Boolean inactive;

    private Boolean warning;

    private Integer okNumber;

}