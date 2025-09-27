package io.gitee.dqcer.mcdull.system.provider.model.dto.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeptSaveDTO implements DTO {

    @NotBlank
    private String name;
    @NotNull
    private Integer parentId;
    @NotNull
    private Integer sort;
    @NotNull
    private Integer status;
    private String description;
}
