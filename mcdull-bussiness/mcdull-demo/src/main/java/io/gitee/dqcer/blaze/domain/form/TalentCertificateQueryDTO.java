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
public class TalentCertificateQueryDTO extends PagedDTO {

    @Schema(description = "证书级别")
    private Integer certificateLevel;

    @Schema(description = "职称 1/无 2/初级 3/中级 4/高级 5/不限")
    private String title;

    @Schema(description = "证书状态（1/正常 2/不正常）")
    private Integer certificateStatus;

    @Schema(description = "初始/转注（1/无 2/初始 3/转注 4/其它）")
    private Integer initialOrTransfer;

}