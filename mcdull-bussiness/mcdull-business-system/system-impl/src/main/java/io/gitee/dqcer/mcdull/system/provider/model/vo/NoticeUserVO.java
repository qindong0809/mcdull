package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 通知公告 员工查看
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeUserVO extends NoticeVO {

    @Schema(description = "是否查看")
    private Boolean viewFlag;

    @Schema(description = "发布日期")
    private LocalDate publishDate;

}
