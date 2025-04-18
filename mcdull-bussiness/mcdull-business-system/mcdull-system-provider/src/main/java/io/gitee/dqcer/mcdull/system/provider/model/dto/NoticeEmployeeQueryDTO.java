package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 通知公告 员工查询表单
 *
 */
@Data
public class NoticeEmployeeQueryDTO extends PagedDTO {

    @Schema(description = "标题、作者、来源、文号")
    private String keywords;

    @Schema(description = "分类")
    private Integer noticeTypeId;

    @Schema(description = "发布-开始时间")
    private LocalDate publishTimeBegin;

    @Schema(description = "未读标识")
    private Boolean notViewFlag;

    @Schema(description = "发布-截止时间")
    private LocalDate publishTimeEnd;
}
