package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.TimestampEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_login_log
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_login_log")
public class LoginLogEntity extends TimestampEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String loginIp;

    private String loginIpRegion;

    private String userAgent;

    /**
     * {@link  io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum}
     */
    private Integer loginResult;

    private String remark;

}
