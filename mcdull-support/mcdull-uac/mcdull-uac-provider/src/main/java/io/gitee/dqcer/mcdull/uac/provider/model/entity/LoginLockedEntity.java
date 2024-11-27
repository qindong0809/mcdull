package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sys_login_locked
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_login_locked")
public class LoginLockedEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private Integer loginFailCount;

    private Boolean lockFlag;

    private Date loginLockBeginTime;

    private Date loginLockEndTime;

    @TableField(fill = FieldFill.INSERT)
    protected Boolean inactive;
}
