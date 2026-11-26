package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatasourceUpdateDTO extends DatasourceAddDTO {
    @NotNull(message = "数据源ID不能为空")
    private Integer id;
}
