package io.gitee.dqcer.mcdull.system.provider.model.entity.administrator;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("administrator_user")
public class AdminUserEntity extends BaseEntity<Integer> {

    private String loginName;
    private String loginPwd;
    private String actualName;
    private String email;
    private Boolean administratorFlag;
    private Date lastLoginTime;

}
