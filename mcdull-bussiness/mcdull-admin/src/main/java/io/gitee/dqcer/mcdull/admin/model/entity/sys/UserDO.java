package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalTime;

/**
 * 系统用户实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
@TableName("sys_user")
public class UserDO extends BaseDO {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 账户
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 最后登录时间
     */
    private LocalTime lastLoginTime;

    /**
     *  {@link io.gitee.dqcer.mcdull.admin.model.enums.UserTypeEnum}
     */
    private Integer type;

    /**
     * 部门id
     */
    private Long deptId;

    private String status;

}
