package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * sys_login_log
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_login_log")
public class LoginLogEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String loginIp;

    private String loginIpRegion;

    private String userAgent;

    private Integer loginResult;

    private String remark;

}
