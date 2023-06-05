package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.util.ValidateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
* @author dqcer
* @since 2022-11-16
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInsertDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

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

    private Long deptId;

    @EnumsStrValid(value = StatusEnum.class)
    private String status;
}