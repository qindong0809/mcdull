package io.gitee.dqcer.mcdull.system.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
* 系统配置 返回客户端值
*
* @author dqcer
* @since 2024-04-29
*/
@EqualsAndHashCode(callSuper = false)
@Data
public class EmailSendHistoryVO implements VO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer id;

    /**
     * {@link io.gitee.dqcer.mcdull.system.provider.model.enums.EmailTypeEnum}
     */
    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "类型名称")
    private String typeName;

    @Schema(description = "接收人")
    private String sentTo;

    @Schema(description = "抄送人")
    private String cc;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件array")
    private String fileIdArray;

    @Schema(description = "创建时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;


}