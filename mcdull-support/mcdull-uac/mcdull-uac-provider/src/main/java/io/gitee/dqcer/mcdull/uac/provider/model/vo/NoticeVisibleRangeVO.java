package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.swagger.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.NoticeVisitbleRangeDataTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新闻、公告 可见范围数据 VO
 *
 */
@Data
public class NoticeVisibleRangeVO {

    @Schema(description = "可见范围类型 0:部门 1:员工")
    @SchemaEnum(NoticeVisitbleRangeDataTypeEnum.class)
    private Integer dataType;

    @Schema(description = "员工/部门id")
    private Integer dataId;

    @Schema(description = "员工/部门 名称")
    private String dataName;

}
