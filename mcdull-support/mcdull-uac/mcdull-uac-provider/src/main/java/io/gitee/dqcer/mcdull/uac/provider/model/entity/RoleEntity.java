package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_role")
@Getter
@Setter
public class RoleEntity extends BaseEntity<Integer> {

    private String roleName;

    private String roleCode;

    private String remark;
}