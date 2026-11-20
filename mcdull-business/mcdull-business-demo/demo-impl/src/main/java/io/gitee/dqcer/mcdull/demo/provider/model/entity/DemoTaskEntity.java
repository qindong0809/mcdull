package io.gitee.dqcer.mcdull.demo.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 演示任务持久化对象。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("demo_task")
public class DemoTaskEntity extends BaseEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String title;

    private String content;

    private Integer status;

    private Integer priority;

    private Date dueTime;
}
