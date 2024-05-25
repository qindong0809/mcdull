package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Getter
@Setter
public class UserUpdateDTO extends UserAddDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "员工id")
    @NotNull(message = "员工id不能为空")
    private Integer employeeId;
}