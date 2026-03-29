package io.gitee.dqcer.mcdull.focus.model.dto;

import io.gitee.dqcer.mcdull.focus.model.enums.TaskBatchBuildDateEnum;
import io.gitee.dqcer.mcdull.focus.model.enums.TaskCategoryEnum;
import io.gitee.dqcer.mcdull.focus.model.enums.TaskPriorityEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Getter
@Setter
public class TaskCreateDTO {
    @NotBlank
    @Length(min = 1, max = 100)
    private String name;
    @Length(min = 1, max = 1024)
    private String description;
    @NotNull
    private Date dueDate;
    @EnumsIntValid(value = TaskPriorityEnum.class)
    private Integer priority;
    @EnumsStrValid(value = TaskCategoryEnum.class)
    private String category;
    private Boolean reminderEnabled;
    @EnumsIntValid(value = TaskBatchBuildDateEnum.class)
    private Integer batchBuildDate;
}
