package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单合同 更新表单
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Data
public class BlazeOrderUpdateDTO implements DTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "id 不能为空")
    private Integer id;

    @Schema(description = "所属人才证书", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属人才证书 不能为空")
    private Integer talentCertId;

    @Schema(description = "所属企业证书", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属企业证书 不能为空")
    private Integer customerCertId;

    @Schema(description = "合同时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同时间 不能为空")
    private Date contractTime;


    private Date startDate;

    private Date endDate;

    private Boolean approve;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "状态（true/已失活 false/未失活）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态（true/已失活 false/未失活） 不能为空")
    private Boolean inactive;

    @NotNull
    private Integer responsibleUserId;

    private List<Integer> deleteFileIdList;

}