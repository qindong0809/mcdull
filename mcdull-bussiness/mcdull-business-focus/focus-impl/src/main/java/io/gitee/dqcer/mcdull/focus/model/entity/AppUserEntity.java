package io.gitee.dqcer.mcdull.focus.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用用户实体
 *
 * @author dqcer
 * @since 2026/03/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_user")
public class AppUserEntity extends BaseEntity<Integer> {

    private String loginName;
    private String loginPwd;
    private String email;

}
