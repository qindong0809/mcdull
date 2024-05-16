package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录查询日志
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginLogQueryDTO extends PagedDTO {

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "ip")
    private String ip;

}
