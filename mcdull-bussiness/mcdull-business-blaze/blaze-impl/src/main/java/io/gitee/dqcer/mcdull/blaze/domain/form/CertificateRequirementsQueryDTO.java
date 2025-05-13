package io.gitee.dqcer.mcdull.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 证书需求表 分页查询表单
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CertificateRequirementsQueryDTO extends PagedDTO implements PermissionDTO{

    @Schema(description = "证书级别")
    private Integer certificateLevel;

    @Schema(description = "初始/转注（1/无 2/初始 3/转注 4/其它）")
    private Integer initialOrTransfer;

    private Integer specialty;

    private Integer socialSecurityRequirement;

    private Integer approve;

    private Integer id;

    private List<Integer> responsibleUserIdList;
}