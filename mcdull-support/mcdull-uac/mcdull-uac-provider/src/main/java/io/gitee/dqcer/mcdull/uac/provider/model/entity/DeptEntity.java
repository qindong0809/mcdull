package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dept")
public class DeptEntity extends BaseEntity {

    private String name;

    private Integer parentId;

    private Integer sort;

    private String phone;

    private Integer principal;

    private String email;

    private String type;

    private String remark;
}
