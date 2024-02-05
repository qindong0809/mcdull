package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import org.hibernate.validator.constraints.Length;

/**
 * @author dqcer
 * @since 2022-11-16
 */
public class UserUpdatePasswordDTO implements DTO {

    private static final long serialVersionUID = 1L;

     @Length(min = 5)
     private String oldPassword;

     @Length(min = 5)
     private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}