package io.gitee.dqcer.mcdull.uac.provider.model.bo;

import io.gitee.dqcer.mcdull.framework.base.support.BO;
import lombok.Data;

/**
 * 电子邮件配置bo
 *
 * @author dqcer
 * @since 2024/06/13
 */
@Data
public class EmailConfigBO implements BO {

    private String host;

    /**
     * 主机端口
     */
    private String port;

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 设置发送方
     */
    private String from;
}
