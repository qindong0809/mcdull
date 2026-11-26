package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DatasourceAddDTO {
    @NotBlank(message = "数据源名称不能为空")
    private String datasourceName;
    @NotBlank(message = "JDBC URL不能为空")
    private String jdbcUrl;
    @NotBlank(message = "数据库账号不能为空")
    private String username;
    private String password;
    private String remark;
}
