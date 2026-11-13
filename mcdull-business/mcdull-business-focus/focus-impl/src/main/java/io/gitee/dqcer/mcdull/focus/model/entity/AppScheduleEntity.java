package io.gitee.dqcer.mcdull.focus.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_schedule")
public class AppScheduleEntity extends BaseEntity<Integer> {

    private String title;
    private Date startTime;
    private Date endTime;
    private String location;
    private Integer priority;
    private Integer repeatType;
    private Integer remindMinutes;
}
