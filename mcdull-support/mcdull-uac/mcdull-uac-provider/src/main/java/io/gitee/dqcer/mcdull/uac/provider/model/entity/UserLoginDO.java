package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;

import java.util.Date;

/**
 * 用户登录实体
 *
 * @author dqcer
 * @since 2022/12/18
 */
@TableName("sys_user_login")
public class UserLoginDO extends IdDO {

    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER )
    private Date createdTime;

    /**
     * 菜单id
     */
    private String account;

    /**
     * token
     */
    private String remark;

    /**
     * 类型（1/登录 2/注销）
     */
    private Integer type;

    private String browser;

    private String os;

    private String operationType;


}
