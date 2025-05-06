package io.gitee.dqcer.mcdull.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单合同 分页查询表单
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlazeOrderQueryDTO extends PagedDTO {

    @Schema(description = "所属人才证书")
    private Integer talentCertId;

    @Schema(description = "所属企业证书")
    private Integer customerCertId;

    @Schema(description = "合同时间")
    private Date contractTimeBegin;

    @Schema(description = "合同时间")
    private Date contractTimeEnd;

    private Integer approve;

}