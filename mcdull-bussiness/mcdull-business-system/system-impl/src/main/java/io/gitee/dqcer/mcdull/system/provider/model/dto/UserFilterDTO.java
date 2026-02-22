package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 地区查询dto
 *
 * @author dqcer
 * @since 2024/06/18
 */
@Data
public class UserFilterDTO implements DTO {

    @NotBlank
    private String categoryCode;
    private String subCategoryCode;
    @NotBlank
    private String filterTitle;
    @NotBlank
    private String filterContent;

}
