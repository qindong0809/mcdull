package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.uac.provider.config.FileKeyVoDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 系统更新日志 新建表单
 *
 */
@Data
public class NoticeAddDTO implements DTO {

    @Schema(description = "类型1公告 2动态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "类型1公告 2动态 不能为空")
    private Integer noticeTypeId;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题 不能为空")
    private String title;

    @Schema(description = "是否全部可见", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否全部可见 不能为空")
    private Boolean allVisibleFlag;

    @Schema(description = "是否定时发布", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否定时发布 不能为空")
    private Boolean scheduledPublishFlag;

    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发布时间 不能为空")
    private LocalDateTime publishTime;

    @Schema(description = "文本内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文本内容 不能为空")
    private String contentText;

    @Schema(description = "html内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "html内容 不能为空")
    private String contentHtml;

    @Schema(description = "附件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "附件 不能为空")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @Schema(description = "页面浏览量pv", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "页面浏览量pv 不能为空")
    private Integer pageViewCount;

    @Schema(description = "用户浏览量uv", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户浏览量uv 不能为空")
    private Integer userViewCount;

    @Schema(description = "来源", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "来源 不能为空")
    private String source;

    @Schema(description = "作者", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "作者 不能为空")
    private String author;

    @Schema(description = "文号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文号 不能为空")
    private String documentNumber;

    @Schema(description = "状态（true/已失活 false/未失活）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态（true/已失活 false/未失活） 不能为空")
    private Boolean inactive;

}