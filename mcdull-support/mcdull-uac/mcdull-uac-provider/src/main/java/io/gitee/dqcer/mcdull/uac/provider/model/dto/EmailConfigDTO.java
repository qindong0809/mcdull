package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author dqcer
 * @since 2024/06/03
 */
@Data
public class EmailConfigDTO implements DTO {

    @NotEmpty
    private String emailHost;

    /**
     * 主机端口
     */
    @NotNull
    private Integer emailPort;

    /**
     * 账户
     */
    @NotEmpty
    private String emailUsername;

    /**
     * 密码
     */
    @NotEmpty
    private String emailPassword;

    /**
     * 设置发送方
     */
    @NotEmpty
    private String emailFrom;
}
