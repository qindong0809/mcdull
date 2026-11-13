package io.gitee.dqcer.mcdull.blaze.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_user")
public class AppUserEntity extends BaseEntity<Integer> {

    private String loginName;
    private String loginPwd;
    private String email;

}
