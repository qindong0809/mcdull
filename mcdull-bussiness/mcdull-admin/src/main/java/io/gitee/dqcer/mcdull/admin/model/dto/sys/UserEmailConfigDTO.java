package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* @author dqcer
* @since 2022-11-16
*/
@Data
public class UserEmailConfigDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String host;

    @NotNull
    private Integer port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}