package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 员工更新部门
 *
 */
@Data
public class UserBatchUpdateDepartmentDTO implements DTO {

    @Schema(description = "员工id")
    @NotEmpty(message = "员工id不能为空")
    @Size(max = 99, message = "一次最多调整99个员工")
    private List<Integer> employeeIdList;

    @Schema(description = "部门ID")
    @NotNull(message = "部门ID不能为空")
    private Integer departmentId;
}
