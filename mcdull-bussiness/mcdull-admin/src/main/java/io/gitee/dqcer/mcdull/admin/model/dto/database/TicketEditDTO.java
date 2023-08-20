package io.gitee.dqcer.mcdull.admin.model.dto.database;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
*  接收客户端参数
*
* @author dqcer
* @since 2023-08-17
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketEditDTO extends TicketAddDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id;

}