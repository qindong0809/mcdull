package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 系统更新日志 新建表单
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeUpdateDTO extends NoticeAddDTO {

    @Schema(description = "id")
    @NotNull(message = "通知id不能为空")
    private Integer noticeId;
}