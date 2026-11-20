package io.gitee.dqcer.mcdull.demo.facade.model.dto;

import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskPriorityEnum;
import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskStatusEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 新建演示任务入参。
 */
@Getter
@Setter
public class DemoTaskCreateDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "任务标题不能为空")
    @Length(max = 64, message = "任务标题不能超过 64 个字符")
    private String title;

    @Length(max = 2000, message = "任务内容不能超过 2000 个字符")
    private String content;

    @NotNull(message = "任务状态不能为空")
    @EnumsIntValid(DemoTaskStatusEnum.class)
    @SchemaEnum(DemoTaskStatusEnum.class)
    private Integer status;

    @NotNull(message = "任务优先级不能为空")
    @EnumsIntValid(DemoTaskPriorityEnum.class)
    @SchemaEnum(DemoTaskPriorityEnum.class)
    private Integer priority;

    private Date dueTime;
}
