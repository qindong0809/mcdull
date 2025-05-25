package io.gitee.mcdull.tools.web.domain;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DatabaseImportDTO implements DTO {

    @NotBlank
    private String ip;
    @NotNull
    private Integer port;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String sql;
}
