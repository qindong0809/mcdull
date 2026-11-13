package io.gitee.dqcer.mcdull.focus.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_focus")
public class AppFocusEntity extends BaseEntity<Integer> {

    private Integer durationMinutes;
    /**
     * {@link io.gitee.dqcer.mcdull.focus.model.enums.FocusModeEnum}
     */
    private Integer mode;
    private Integer taskId;
}
