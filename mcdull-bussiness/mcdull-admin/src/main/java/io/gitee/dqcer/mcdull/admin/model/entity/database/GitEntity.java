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
@TableName("database_git")
public class GitEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String url;

    private String username;

    private String password;

    private String storePath;

    private String executePath;

    private String status;
}
