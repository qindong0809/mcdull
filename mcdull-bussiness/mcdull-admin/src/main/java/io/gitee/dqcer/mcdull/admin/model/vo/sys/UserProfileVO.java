package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.Date;

/**
 * 用户视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class UserProfileVO implements VO {

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    private Long id;

    /**
     * 状态
     * @see StatusEnum
     */
    private String status;

    /**
     * 账户
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;


    /**
     *  类型（1/自定义 2/内置）
     */
    private Integer type;


    private Long deptId;

    private String deptName;

    private Date createdTime;

    private String roleNameList;

    private Boolean isAdmin;
}
