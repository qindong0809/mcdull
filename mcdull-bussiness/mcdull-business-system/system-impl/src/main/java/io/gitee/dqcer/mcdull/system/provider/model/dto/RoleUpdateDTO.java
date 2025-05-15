package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Getter
@Setter
@ToString
public class RoleUpdateDTO extends RoleAddDTO {

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    protected Integer roleId;
   
}