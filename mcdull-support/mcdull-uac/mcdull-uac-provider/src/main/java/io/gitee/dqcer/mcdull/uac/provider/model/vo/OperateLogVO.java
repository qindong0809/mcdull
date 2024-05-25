package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.swagger.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *  操作日志信息
 *
 */
@Data
public class OperateLogVO implements VO {

    @Schema(description = "主键")
    private Integer operateLogId;

    @Schema(description = "用户id")
    private Integer operateUserId;

    @SchemaEnum(value = UserTypeEnum.class, desc = "用户类型")
    private Integer operateUserType;

    @Schema(description = "用户名称")
    private String operateUserName;

    @Schema(description = "Trace ID")
    private String traceId;

    @Schema(description = "耗时(ms)")
    private Integer timeTaken;

    @Schema(description = "操作模块")
    private String module;

    @Schema(description = "操作内容")
    private String content;

    @Schema(description = "请求路径")
    private String url;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求参数")
    private String param;

    @Schema(description = "客户ip")
    private String ip;

    @Schema(description = "客户ip地区")
    private String ipRegion;

    @Schema(description = "user-agent")
    private String userAgent;

    @Schema(description = "请求结果 0失败 1成功")
    private Boolean successFlag;

    @Schema(description = "失败原因")
    private String failReason;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
