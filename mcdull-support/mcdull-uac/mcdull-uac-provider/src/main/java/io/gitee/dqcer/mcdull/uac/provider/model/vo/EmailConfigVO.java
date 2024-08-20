package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 电子邮件配置bo
 *
 * @author dqcer
 * @since 2024/06/13
 */
@Data
public class EmailConfigVO implements VO {

    @Schema(description = "地址")
    private String emailHost;

    @Schema(description = "端口")
    private Integer emailPort;

    @Schema(description = "用户名")
    private String emailUsername;

    @Schema(description = "密码")
    private String emailPassword;

    @Schema(description = "发送方")
    private String emailFrom;
}
