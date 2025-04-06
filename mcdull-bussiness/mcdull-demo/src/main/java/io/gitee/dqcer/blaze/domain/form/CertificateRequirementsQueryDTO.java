package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 证书需求表 分页查询表单
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CertificateRequirementsQueryDTO extends PagedDTO {

    @Schema(description = "证书级别")
    private Integer certificateLevel;

    @Schema(description = "初始/转注（1/无 2/初始 3/转注 4/其它）")
    private Integer initialOrTransfer;

    private Integer specialty;

    private Integer socialSecurityRequirement;

    private Integer approve;

}