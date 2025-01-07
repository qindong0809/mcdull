package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业信息 分页查询表单
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerInfoQueryDTO extends PagedDTO {

    @Schema(description = "企业名称")
    private String name;

    @Schema(description = "客户类型")
    private String customerType;

    @Schema(description = "状态（true/已失活 false/未失活）")
    private Boolean inactive;

}