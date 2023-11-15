package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;

import java.time.LocalTime;

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
     *  类型（1/自定义 2/内置）
     */
    private Integer type;

    private Integer status;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDO{");
        sb.append("delFlag=").append(delFlag);
        sb.append(", nickname='").append(nickName).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", salt='").append(salt).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", type=").append(type);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", status=").append(status);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
