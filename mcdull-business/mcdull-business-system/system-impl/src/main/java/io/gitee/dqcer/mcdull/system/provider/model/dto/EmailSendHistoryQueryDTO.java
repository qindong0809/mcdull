package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 分页查询 系统配置
 *
 * @author dqcer
 * @since 2024/04/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmailSendHistoryQueryDTO extends PagedDTO {

    @Length(max = 50)
    @Schema(description = "接收人")
    private String sendTo;
}
