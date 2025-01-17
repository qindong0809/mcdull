package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.util.ValidateUtil;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

/**
* @author dqcer
* @since 2022-11-16
*/
@Data
public class UserInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String nickName;

    @NotBlank
    private String account;

    @NotBlank
    private String password;

    @Email
    private String email;

    @Pattern(regexp = ValidateUtil.REGEXP_PHONE)
    private String phone;

    @NotNull
    private Long deptId;

    @EnumsStrValid(value = StatusEnum.class)
    private String status;

    private List<Long> roleIds;

    private List<Long> postIds;
}