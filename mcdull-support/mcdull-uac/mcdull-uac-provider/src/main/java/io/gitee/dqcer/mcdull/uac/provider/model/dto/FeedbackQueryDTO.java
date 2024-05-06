package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 意见反馈 查询
 *
 * @author dqcer
 */
@Data
public class FeedbackQueryDTO extends PagedDTO {

    @Schema(description = "搜索词")
    @Length(max = 25, message = "搜索词最多25字符")
    private String searchWord;

    @Schema(description = "开始时间", example = "2021-02-14")
    private LocalDate startDate;

    @Schema(description = "截止时间", example = "2022-10-15")
    private LocalDate endDate;
}
