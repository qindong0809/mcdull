package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys department
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_department")
@Getter
@Setter
public class DepartmentEntity extends BaseEntity<Long> {

    private String name;

    private Long managerId;

    private Long parentId;

    private Integer sort;
}