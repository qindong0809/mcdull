package io.gitee.dqcer.mcdull.system.provider.model.entity.administrator;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("administrator_user_menu")
public class AdminUserMenuEntity extends RelEntity<Integer> {

    private Integer userId;
    private Integer menuId;
}
