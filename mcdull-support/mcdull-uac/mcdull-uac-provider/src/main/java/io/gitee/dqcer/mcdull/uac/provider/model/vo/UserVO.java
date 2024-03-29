package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 用户视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
@ToString
public class UserVO implements VO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private Integer createdBy;

    private String createdByStr;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private Integer updatedBy;

    private String updatedByStr;

    /**
     * 状态
     * @see StatusEnum
     */
    private String status;

//    @Transform(from = "status", param = "status_type")
    private String statusStr;


    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 删除str
     */
//    @Transform(from = "delFlag", param = "del_flag_type")
    private String delFlagStr;

    /**
     * 账户
     */
    private String account;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;


    /**
     *  类型（1/自定义 2/内置）
     */
    private Integer type;

//    @Transform(from = "type", param = "data_type")
    private String typeStr;

    /**
     * 角色集
     */
    private List<BaseVO<Integer, String>> roles;

}
