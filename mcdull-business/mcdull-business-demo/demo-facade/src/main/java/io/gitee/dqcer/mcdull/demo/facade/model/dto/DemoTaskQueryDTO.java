package io.gitee.dqcer.mcdull.demo.facade.model.dto;

import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskStatusEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * 演示任务分页查询入参。
 */
@Getter
@Setter
public class DemoTaskQueryDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private String title;

    @EnumsIntValid(value = DemoTaskStatusEnum.class, required = false)
    @SchemaEnum(value = DemoTaskStatusEnum.class, required = false)
    private Integer status;
}
