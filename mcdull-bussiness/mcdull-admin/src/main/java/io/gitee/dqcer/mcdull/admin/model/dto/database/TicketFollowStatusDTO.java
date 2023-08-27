package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.admin.model.enums.TicketFollowStatusEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PkDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*  接收客户端参数
 *
* @author dqcer
* @since 2023-08-17
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketFollowStatusDTO extends PkDTO {

    private static final long serialVersionUID = 1L;

    @EnumsIntValid(required = true, value = TicketFollowStatusEnum.class)
    private Integer status;
}