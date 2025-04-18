package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *  操作日志查询 表单
 *
 */
@Data
public class OperateLogQueryDTO extends PagedDTO {


    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;


    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "请求结果 false失败 true成功")
    private Boolean successFlag;

    @Schema(description = "请求路径")
    private String url;

    @Schema(description = "链路标识")
    private String traceId;

    @Schema(description = "操作模块")
    private String module;

    @Schema(description = "操作内容")
    private String content;

    @Schema(hidden = true)
    private Integer userId;
}
