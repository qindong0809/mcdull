package io.gitee.dqcer.mcdull.system.provider.model.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.system.provider.model.enums.MessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Data
public class MessageVO implements VO {


    private Integer messageId;

    @SchemaEnum(value = MessageTypeEnum.class)
    private Integer messageType;


    @Schema(description = "接收者id")
    private Integer receiverUserId;

    @Schema(description = "相关业务id")
    private String dataId;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "是否已读")
    private Boolean readFlag;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "已读时间")
    private Date readTime;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "创建时间")
    private Date createdTime;

}
