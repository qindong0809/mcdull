package io.gitee.dqcer.mcdull.system.provider.model.dto.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleSaveDTO implements DTO {

    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private String description;
    private Integer dataScope;
    private Integer sort;
}
