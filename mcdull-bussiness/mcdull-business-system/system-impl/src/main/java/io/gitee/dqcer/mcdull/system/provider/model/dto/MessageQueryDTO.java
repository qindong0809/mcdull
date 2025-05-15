package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.system.provider.model.enums.MessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 系统更新日志 查询
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageQueryDTO extends PagedDTO {

    @Schema(description = "搜索词")
    @Length(max = 50, message = "搜索词最多50字符")
    private String searchWord;

    @SchemaEnum(value = MessageTypeEnum.class)
    @EnumsIntValid(value = MessageTypeEnum.class, required = false, message = "消息类型")
    private Integer messageType;

    @Schema(description = "是否已读")
    private Boolean readFlag;

    @Schema(description = "查询开始时间")
    private Date startDate;

    @Schema(description = "查询结束时间")
    private Date endDate;

    @Schema(hidden = true)
    private Integer receiverUserId;

}