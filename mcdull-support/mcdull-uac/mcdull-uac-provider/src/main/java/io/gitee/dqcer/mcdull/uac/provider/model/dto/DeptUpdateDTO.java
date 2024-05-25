package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
* @author dqcer
* @since 2022-11-16
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class DeptUpdateDTO extends DeptInsertDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门id")
    @NotNull(message = "部门id不能为空")
    private Integer departmentId;

}