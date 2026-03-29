package io.gitee.dqcer.mcdull.focus.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO extends TaskCreateDTO{
    @NotNull
    private Integer id;
}
