package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.uac.provider.config.FileKeyVoSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * 用于更新 【通知、公告】 的 VO 对象
 *
 */
@Data
public class NoticeUpdateFormVO extends NoticeVO {

    @Schema(description = "纯文本内容")
    private String contentText;

    @Schema(description = "html内容")
    private String contentHtml;

    @Schema(description = "附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @Schema(description = "可见范围")
    private List<NoticeVisibleRangeVO> visibleRangeList;

}
