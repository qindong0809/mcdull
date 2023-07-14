package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author dqcer
 * @since 2022-11-16
 */
@Data
public class InstanceAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long groupId;

    @NotBlank
    private String name;

    @NotBlank
    @Length(max = 128)
    private String host;

    @NotNull
    private Integer port;

    private String databaseName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @EnumsStrValid(required = true, value = StatusEnum.class)
    private String status;
}