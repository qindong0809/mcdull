package io.gitee.dqcer.mcdull.demo.facade.model.vo;

import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskPriorityEnum;
import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskStatusEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 演示任务视图对象。
 */
@Getter
@Setter
public class DemoTaskVO implements VO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private String content;

    @SchemaEnum(DemoTaskStatusEnum.class)
    private Integer status;

    /**
     * 状态文本由实现层翻译后填充，契约包不依赖字典池。
     */
    private String statusName;

    @SchemaEnum(DemoTaskPriorityEnum.class)
    private Integer priority;

    private String priorityName;

    private Date dueTime;

    private Date createdTime;

    private Date updatedTime;
}
