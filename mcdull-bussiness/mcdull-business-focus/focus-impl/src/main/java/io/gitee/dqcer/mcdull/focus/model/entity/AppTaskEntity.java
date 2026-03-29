package io.gitee.dqcer.mcdull.focus.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_task")
public class AppTaskEntity extends BaseEntity<Integer> {

    private String name;
    private String description;
    private Date dueDate;
    /**
     * {@link  io.gitee.dqcer.mcdull.focus.model.enums.TaskPriorityEnum}
     */
    private Integer priority;

    /**
     * {@link  io.gitee.dqcer.mcdull.focus.model.enums.TaskCategoryEnum}
     */
    private String category;
    private Boolean completed;
    private Boolean reminderEnabled;
}
