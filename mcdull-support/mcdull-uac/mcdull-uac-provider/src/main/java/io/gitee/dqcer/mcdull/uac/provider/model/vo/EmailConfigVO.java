package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.BO;
import lombok.Data;

/**
 * 电子邮件配置bo
 *
 * @author dqcer
 * @since 2024/06/13
 */
@Data
public class EmailConfigVO implements BO {

    private String emailHost;

    /**
     * 主机端口
     */
    private Integer emailPort;

    /**
     * 账户
     */
    private String emailUsername;

    /**
     * 密码
     */
    private String emailPassword;

    /**
     * 设置发送方
     */
    private String emailFrom;
}
