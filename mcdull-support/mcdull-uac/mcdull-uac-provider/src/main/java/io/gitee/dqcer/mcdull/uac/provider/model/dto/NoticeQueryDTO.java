package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 系统更新日志 新建表单
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeQueryDTO extends PagedDTO {

    @Schema(description = "分类")
    private Integer noticeTypeId;

    @Schema(description = "标题、作者、来源")
    private String keywords;

    @Schema(description = "文号")
    private String documentNumber;

    @Schema(description = "创建人")
    private Integer createUserId;

    @Schema(description = "删除标识")
    private Boolean deletedFlag;

    @Schema(description = "创建-开始时间")
    private LocalDate createTimeBegin;

    @Schema(description = "创建-截止时间")
    private LocalDate createTimeEnd;

    @Schema(description = "发布-开始时间")
    private LocalDate publishTimeBegin;

    @Schema(description = "发布-截止时间")
    private LocalDate publishTimeEnd;

}