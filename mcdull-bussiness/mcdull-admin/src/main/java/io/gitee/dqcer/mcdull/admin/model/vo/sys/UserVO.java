package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeStringConverter;
import io.gitee.dqcer.mcdull.admin.framework.transformer.DictTransformer;
import io.gitee.dqcer.mcdull.admin.framework.transformer.UserTransformer;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 用户视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class UserVO implements VO {

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    @ExcelIgnore
    private Long id;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private Date createdTime;

    /**
     * 创建人
     */
    @ExcelProperty("创建人")
    private Long createdBy;

    @Transform(from = "createdBy", transformer = UserTransformer.class)
    private String createdByStr;

    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    private Date updatedTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    @ExcelProperty("更新人")
    @Transform(from = "updatedBy", transformer = UserTransformer.class)
    private String updatedByStr;

    /**
     * 状态
     * @see StatusEnum
     */
    private String status;

    @Transform(from = "status", param = "status_type", transformer = DictTransformer.class)
    private String statusStr;


    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 删除str
     */
    @Transform(from = "delFlag", param = "del_flag_type", transformer = DictTransformer.class)
    private String delFlagStr;

    /**
     * 账户
     */
    @ExcelProperty("账户")
    private String account;

    /**
     * 昵称
     */
    @ExcelProperty("昵称")
    private String nickName;

    /**
     * 电子邮件
     */
    @ExcelProperty("电子邮件")
    private String email;

    /**
     * 电话
     */
    @ExcelProperty("电话")
    private String phone;

    /**
     * 最后登录时间
     */
    @ExcelProperty(value = "最后登录时间",converter = LocalDateTimeStringConverter.class)
    private LocalDateTime lastLoginTime;


    /**
     *  类型（1/自定义 2/内置）
     */
    @ExcelIgnore
    private Integer type;

    @Transform(from = "type", param = "data_type", transformer = DictTransformer.class)
    private String typeStr;

    /**
     * 角色集
     */
    @ExcelIgnore
    private List<BaseVO<Long, String>> roles;

    private Long deptId;

    @ExcelProperty("部门")
    private String deptName;


}
