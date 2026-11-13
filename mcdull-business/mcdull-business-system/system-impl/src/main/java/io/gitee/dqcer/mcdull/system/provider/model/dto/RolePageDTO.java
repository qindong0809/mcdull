package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class RolePageDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private Boolean inactive;

    @Length(max = 128)
    private String name;

}