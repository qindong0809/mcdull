package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TenantAddDTO {
    @NotBlank(message = "租户名称不能为空")
    private String tenantName;
    @NotNull(message = "数据源不能为空")
    private Integer datasourceId;
    @NotNull(message = "归属类型不能为空")
    private Integer ownerType;
    @NotNull(message = "隔离模式不能为空")
    private Integer isolationMode;
    private Date expireTime;
    private List<String> applicationCode;
    private String remark;
}
