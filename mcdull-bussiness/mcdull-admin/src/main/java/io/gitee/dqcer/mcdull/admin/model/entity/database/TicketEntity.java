package io.gitee.dqcer.mcdull.admin.model.entity.database;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * database group
 *
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("database_ticket")
public class TicketEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String number;

    private Long groupId;

    private Integer executeType;

    private Integer hasMerge;

    private String remark;

    private String sqlScript;

    private Integer followStatus;

    private Integer cancelStatus;
}
