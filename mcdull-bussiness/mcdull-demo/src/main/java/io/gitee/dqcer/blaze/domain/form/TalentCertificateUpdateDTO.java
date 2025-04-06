package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 证书需求表 更新表单
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

@Data
public class TalentCertificateUpdateDTO implements DTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "id 不能为空")
    private Integer id;

    @Schema(description = "所属人才", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属人才 不能为空")
    private Integer talentId;

    @Schema(description = "证书级别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "证书级别 不能为空")
    private Integer certificateLevel;

    @Schema(description = "专业", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "专业 不能为空")
    private Integer specialty;

    @Schema(description = "单位所在省", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单位所在省 不能为空")
    private String provincesCode;

    @Schema(description = "单位所在市", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单位所在市 不能为空")
    private String cityCode;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量 不能为空")
    private Integer quantity;

    @Schema(description = "职称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职称 不能为空")
    private Integer title;

    @Schema(description = "初始/转注", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "初始/转注 不能为空")
    private Integer initialOrTransfer;

    @Schema(description = "证书状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "证书状态 不能为空")
    private Integer certificateStatus;

    @Schema(description = "职位合同价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职位合同价 不能为空")
    private BigDecimal positionContractPrice;

    @Schema(description = "其他费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "其他费用 不能为空")
    private BigDecimal otherCosts;

    @Schema(description = "职位实际价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职位实际价 不能为空")
    private BigDecimal actualPositionPrice;

    @Schema(description = "期限（月）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "期限（月） 不能为空")
    private Integer duration;

    @Schema(description = "招标出场", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "招标出场 不能为空")
    private Integer biddingExit;

    @Schema(description = "三类人员", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "三类人员 不能为空")
    private Integer threePersonnel;

    @Schema(description = "社保要求", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "社保要求 不能为空")
    private Integer socialSecurityRequirement;

    @Schema(description = "职位来源", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职位来源 不能为空")
    private Integer positionSource;

    @Schema(description = "备注")
    private String remarks;

    private Integer delFileId;

    @Schema(description = "审批人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer responsibleUserId;

}