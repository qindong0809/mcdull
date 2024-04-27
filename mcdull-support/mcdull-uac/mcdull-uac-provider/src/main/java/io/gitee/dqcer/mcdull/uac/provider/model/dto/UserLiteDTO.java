package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import lombok.Getter;
import lombok.Setter;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Setter
@Getter
public class UserLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

}