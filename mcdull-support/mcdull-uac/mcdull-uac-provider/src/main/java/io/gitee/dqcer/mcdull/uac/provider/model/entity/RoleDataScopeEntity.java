package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dqcer
 * @since 2024/05/13
 */
@Getter
@Setter
@TableName("sys_role_data_scope")
public class RoleDataScopeEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer dataScopeType;

    private Integer viewType;

    private Long roleId;
}