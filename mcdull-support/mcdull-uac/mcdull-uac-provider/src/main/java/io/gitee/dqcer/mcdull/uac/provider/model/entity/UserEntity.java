package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统用户实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_user")
@Getter
@Setter
public class UserEntity extends BaseEntity<Integer> {

    private String loginName;

    private String loginPwd;

    private String actualName;

    private String email;

    private Integer gender;

    private String phone;

    private Integer departmentId;

    private Boolean administratorFlag;

    private Date lastLoginTime;

    private Date lastPasswordModifiedDate;

    private String usedPassword;

    private String remark;
}
