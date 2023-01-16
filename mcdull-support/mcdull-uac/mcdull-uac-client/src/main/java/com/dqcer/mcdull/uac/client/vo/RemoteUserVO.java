package io.gitee.dqcer.uac.client.vo;

import io.gitee.dqcer.framework.base.annotation.Transform;
import io.gitee.dqcer.framework.base.vo.BaseVO;
import io.gitee.dqcer.framework.base.vo.VO;
import io.gitee.dqcer.framework.base.enums.StatusEnum;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * 用户视图对象
 *
 * @author dqcer
 * @version  2022/11/27
 */
public class RemoteUserVO implements VO {

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private Long createdBy;

    private String createdByStr;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    private String updatedByStr;

    /**
     * 状态
     * @see StatusEnum
     */
    private Integer status;

    @Transform(from = "status", param = "status_type")
    private String statusStr;


    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 删除str
     */
    @Transform(from = "delFlag", param = "del_flag_type")
    private String delFlagStr;

    /**
     * 账户
     */
    private String account;

    /**
     * 昵称
     */
    private String nickname;

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

    @Transform(from = "type", param = "data_type")
    private String typeStr;

    /**
     * 角色集
     */
    private List<BaseVO> roles;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RemoteUserVO{");
        sb.append("id=").append(id);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createdByStr='").append(createdByStr).append('\'');
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedByStr='").append(updatedByStr).append('\'');
        sb.append(", status=").append(status);
        sb.append(", statusStr='").append(statusStr).append('\'');
        sb.append(", delFlag=").append(delFlag);
        sb.append(", delFlagStr='").append(delFlagStr).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", type=").append(type);
        sb.append(", typeStr='").append(typeStr).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }

    public List<BaseVO> getRoles() {
        return roles;
    }

    public void setRoles(List<BaseVO> roles) {
        this.roles = roles;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreatedByStr() {
        return createdByStr;
    }

    public void setCreatedByStr(String createdByStr) {
        this.createdByStr = createdByStr;
    }

    public String getUpdatedByStr() {
        return updatedByStr;
    }

    public void setUpdatedByStr(String updatedByStr) {
        this.updatedByStr = updatedByStr;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDelFlagStr() {
        return delFlagStr;
    }

    public void setDelFlagStr(String delFlagStr) {
        this.delFlagStr = delFlagStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
