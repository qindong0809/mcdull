package io.gitee.dqcer.mcdull.admin.model.entity.database;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * database ticket instance
 *
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("database_ticket_instance")
public class TicketInstanceEntity extends RelEntity {

    private static final long serialVersionUID = 1L;

    private Long ticketId;

    private Long groupId;

    private Long instanceId;
}
