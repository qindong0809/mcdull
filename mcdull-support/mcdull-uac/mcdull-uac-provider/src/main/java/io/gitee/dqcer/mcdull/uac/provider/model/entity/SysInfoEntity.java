package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_info
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_info")
public class SysInfoEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * 主机ip
     */
    private String emailHost;
    /**
     * 主机端口
     */
    private String emailPort;
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
