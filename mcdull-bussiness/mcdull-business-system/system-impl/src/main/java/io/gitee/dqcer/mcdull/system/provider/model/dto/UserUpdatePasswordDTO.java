package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author dqcer
 * @since 2022-11-16
 */
@ToString
public class UserUpdatePasswordDTO implements DTO {

    private static final long serialVersionUID = 1L;


    @Schema(description = "old password")
    @Length(min = 5)
    private String oldPassword;

    @Schema(description = "new password")
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