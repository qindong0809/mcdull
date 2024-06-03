package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Setter
@Getter
@ToString
public class RoleInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private String roleName;

    private String roleCode;

    private String remark;
   
}