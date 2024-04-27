package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_role_user")
public class RoleUserEntity extends RelEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;

}
