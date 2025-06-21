package io.gitee.dqcer.mcdull.workflow.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.SortOrderEnum;
import io.gitee.dqcer.mcdull.framework.base.support.Paged;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.warm.flow.orm.entity.FlowDefinition;

/**
 * 流程定义 DTO
 *
 * @author dqcer
 * @since 2025/06/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowDefinitionDTO extends FlowDefinition implements Paged {

    /**
     * 每页记录数
     */
    @Max(value = 100000)
    @Min(value = 1)
    protected Integer pageSize = 10;

    /**
     * 当前页数
     */
    @Min(value = 1)
    protected Integer pageNum = 1;

    /**
     * 排序字段信息
     */
    protected String sortField;

    /**
     * 排序方式
     */
    @SchemaEnum(SortOrderEnum.class)
    @EnumsStrValid(required = false, value = SortOrderEnum.class, message = "排序类型 枚举值错误")
    protected String sortOrder;
}
