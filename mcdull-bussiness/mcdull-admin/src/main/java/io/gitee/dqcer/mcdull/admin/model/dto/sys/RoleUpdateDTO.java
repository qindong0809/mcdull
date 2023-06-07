package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * role update
 *
 * @author dqcer
 * @since 2022-11-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleUpdateDTO extends RoleInsertDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long roleId;

}