package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 用户登录实体
 *
 * @author dqcer
 * @since 2022/12/18
 */
@TableName("sys_user_login")
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserLoginDO extends IdDO {

    private static final long serialVersionUID = 1L;

    public static final String OK = "1";

    public static final String FAIL = "2";

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER )
    private Date createdTime;

    /**
     * 账户
     */
    private String account;


    /**
     * 类型（1/成功 2/失败）
     */
    private String type;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作类型
     */
    private String operationType;

}
