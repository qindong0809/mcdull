package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户配置实体
 *
 * @author dqcer
 * @since 2025/03/21
 */
@TableName("sys_user_config")
@Getter
@Setter
public class UserConfigEntity extends BaseEntity<Integer> {

    private Integer userId;

    private String dateFormat;

    private String timezone;
}
