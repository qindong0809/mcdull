package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author dqcer
 * @since 2022-11-16
 */
@Data
public class GroupAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    @EnumsStrValid(required = true, value = StatusEnum.class)
    private String status;
}