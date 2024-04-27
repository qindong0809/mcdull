package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class DeptInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private String name;

    private Long managerId;

    private Long parentId;

    private Integer sort;
}