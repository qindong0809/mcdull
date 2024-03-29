package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.support.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *  user post DO
 *
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
@TableName("sys_user_post")
public class UserPostEntity implements Entity {

    private Long userId;

    private Long postId;
}
