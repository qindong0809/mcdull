package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * 新闻、公告  VO
 *
 */
@Data
public class NoticeVO implements VO {

    @Schema(description = "id")
    private Integer noticeId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "分类")
    private Integer noticeTypeId;

    @Schema(description = "分类名称")
    private String noticeTypeName;

    @Schema(description = "是否全部可见")
    private Boolean allVisibleFlag;

    @Schema(description = "是否定时发布")
    private Boolean scheduledPublishFlag;

    @Schema(description = "发布状态")
    private Boolean publishFlag;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "文号")
    private String documentNumber;

    @Schema(description = "页面浏览量")
    private Integer pageViewCount;

    @Schema(description = "用户浏览量")
    private Integer userViewCount;

    @Schema(description = "删除标识")
    private Boolean deletedFlag;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "创建时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @Schema(description = "更新时间")
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;

}
