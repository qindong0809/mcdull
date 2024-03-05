package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Data
@ToString
@TableName("sys_user_role")
public class UserRoleDO extends IdDO {

    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER )
    private Date createdTime;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;

}
