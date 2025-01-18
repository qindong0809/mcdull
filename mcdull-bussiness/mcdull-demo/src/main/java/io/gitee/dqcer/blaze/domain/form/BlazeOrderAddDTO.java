package io.gitee.dqcer.blaze.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 订单合同 新建表单
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Data
public class BlazeOrderAddDTO implements DTO {

    @Schema(description = "所属人才证书", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属人才证书 不能为空")
    private Integer talentCertId;

    @Schema(description = "所属企业证书", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属企业证书 不能为空")
    private Integer customerCertId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "合同时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同时间 不能为空")
    private Date contractTime;

    private String remarks;


    @Schema(description = "状态（true/已失活 false/未失活）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态（true/已失活 false/未失活） 不能为空")
    private Boolean inactive;

}