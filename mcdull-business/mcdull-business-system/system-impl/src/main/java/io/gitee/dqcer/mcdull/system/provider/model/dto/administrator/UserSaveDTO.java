package io.gitee.dqcer.mcdull.system.provider.model.dto.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserSaveDTO implements DTO {

    @NotNull
    private Integer deptId;
    private String description;
    private String email;
    private Integer gender;
    private String phone;
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    @NotNull
    private List<Integer> roleIds;
    private Integer status;
    @NotBlank
    private String username;
}
