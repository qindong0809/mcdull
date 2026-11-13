package io.gitee.dqcer.mcdull.system.provider.model.entity.administrator;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("administrator_role")
public class AdminRoleEntity extends RelEntity<Integer> {

    private String name;
    private String code;
    /**
     * {@link  io.gitee.dqcer.mcdull.system.provider.model.enums.administrator.AdminRoleDataScopeEnum}
     */
    private Integer dataScope;
    private String description;
    private Integer sort;
    private Boolean isSystem;
    private String menuJoin;
}
