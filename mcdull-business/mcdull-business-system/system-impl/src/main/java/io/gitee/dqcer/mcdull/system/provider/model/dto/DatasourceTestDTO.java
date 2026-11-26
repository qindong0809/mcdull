package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DatasourceTestDTO {
    private Integer id;
    @NotBlank(message = "JDBC URL不能为空")
    private String jdbcUrl;
    @NotBlank(message = "数据库账号不能为空")
    private String username;
    private String password;
}
