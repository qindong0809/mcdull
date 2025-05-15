package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通知公告 类型
 *
 */
@Data
public class NoticeTypeVO {

    @Schema(description = "通知类型id")
    private Integer noticeTypeId;

    @Schema(description = "通知类型-名称")
    private String noticeTypeName;

}
