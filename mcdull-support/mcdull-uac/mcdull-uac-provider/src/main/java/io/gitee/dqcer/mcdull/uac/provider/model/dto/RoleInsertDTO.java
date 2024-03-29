package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class RoleInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;
    
    /**
     * 昵称
     */
    @NotBlank
    @Length(min = 1, max = 512)
    private String name;

    /**
     * 描述
     */
    @Length(max = 2048)
    private String description;

   
}