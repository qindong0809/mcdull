package io.gitee.dqcer.mcdull.focus.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_habit")
public class AppHabitEntity extends BaseEntity<Integer> {

    private String name;
    private String reminderTime;
    private Boolean preset;
}
