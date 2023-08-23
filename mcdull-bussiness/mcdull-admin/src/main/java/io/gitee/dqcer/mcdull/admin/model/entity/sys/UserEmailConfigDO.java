package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
@TableName("sys_user_email_config")
public class UserEmailConfigDO extends BaseDO {

    private Long userId;

    private String host;

    private String username;

    private String password;

    private Integer port;
}
