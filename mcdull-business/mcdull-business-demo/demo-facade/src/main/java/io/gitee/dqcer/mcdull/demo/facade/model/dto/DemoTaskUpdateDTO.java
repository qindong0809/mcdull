package io.gitee.dqcer.mcdull.demo.facade.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改演示任务入参。
 */
@Getter
@Setter
public class DemoTaskUpdateDTO extends DemoTaskCreateDTO {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "任务 ID 不能为空")
    private Integer id;
}
