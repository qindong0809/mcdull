package io.gitee.dqcer.mcdull.admin.model.entity.database;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * database instance
 *
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("database_instance")
public class InstanceEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long groupId;

    private String name;

    private String host;

    private Integer port;

    private String databaseName;

    private String username;

    private String password;

    private String status;
}
