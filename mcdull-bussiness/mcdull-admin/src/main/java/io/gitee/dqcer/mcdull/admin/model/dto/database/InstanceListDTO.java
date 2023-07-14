package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Instance 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InstanceListDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private Long groupId;

}