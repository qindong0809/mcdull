package io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生产进度表 分页查询表单
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductionScheduleQueryDTO extends PagedDTO {

    @Schema(description = "keyword")
    private String keyword;

}