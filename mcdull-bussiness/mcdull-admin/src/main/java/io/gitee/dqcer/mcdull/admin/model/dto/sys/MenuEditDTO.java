package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
* 资源 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuEditDTO extends MenuAddDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id;

}