package io.gitee.dqcer.mcdull.system.provider.model.entity.administrator;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("administrator_dept")
public class AdminDeptEntity extends BaseEntity<Integer> {
    private String name;
    private Integer parentId;
    private String ancestors;
    private String description;
    private Integer sort;
    private Boolean status;
    private Boolean isSystem;
}
