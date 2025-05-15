package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_change_log
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_password_policy")
public class PasswordPolicyEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer repeatablePasswordNumber;

    private Integer failedLoginMaximumNumber;

    private Integer failedLoginMaximumTime;

    private Integer passwordExpiredPeriod;

}
