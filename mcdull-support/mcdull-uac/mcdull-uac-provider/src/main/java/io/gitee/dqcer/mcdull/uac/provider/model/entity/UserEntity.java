package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 系统用户实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_user")
@Data
public class UserEntity extends BaseEntity {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户名
     */
    private String username;

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

    private Integer sex;

    /**
     * 电话
     */
    private String phone;

    private String remark;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     *  类型（1/自定义 2/内置）
     */
    private Integer type;

    private Integer deptId;
}
