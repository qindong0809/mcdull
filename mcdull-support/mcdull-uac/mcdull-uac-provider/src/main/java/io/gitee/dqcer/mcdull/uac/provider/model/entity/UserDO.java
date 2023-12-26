package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;

import java.time.LocalTime;
import java.util.StringJoiner;

/**
 * 系统用户实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_user")
public class UserDO extends BaseDO {

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

    /**
     * 电话
     */
    private String phone;

    /**
     * 最后登录时间
     */
    private LocalTime lastLoginTime;

    /**
     *  类型（1/自定义 2/内置）
     */
    private Integer type;


    @Override
    public String toString() {
        return new StringJoiner(", ", UserDO.class.getSimpleName() + "[", "]")
                .add("nickName='" + nickName + "'")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("salt='" + salt + "'")
                .add("email='" + email + "'")
                .add("phone='" + phone + "'")
                .add("lastLoginTime=" + lastLoginTime)
                .add("type=" + type)
                .add("createdBy=" + createdBy)
                .add("updatedBy=" + updatedBy)
                .add("inactive=" + inactive)
                .add("createdTime=" + createdTime)
                .add("updatedTime=" + updatedTime)
                .add("delFlag=" + delFlag)
                .add("id=" + id)
                .toString();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}
