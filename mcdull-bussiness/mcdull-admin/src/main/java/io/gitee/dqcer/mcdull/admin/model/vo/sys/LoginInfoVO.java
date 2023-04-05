package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import com.alibaba.excel.annotation.ExcelProperty;
import io.gitee.dqcer.mcdull.admin.model.enums.LoginOperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
* 登录日志记录 返回客户端值
*
* @author dqcer
* @since 2023-01-14
*/
@Data
public class LoginInfoVO implements VO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户账号
     */
    @ExcelProperty(value = "用户账号", index = 0)
    private String account;

    /**
     * 类型（1/成功 2/失败）
     */
    private String type;

    /**
     * 浏览器
     */
    @ExcelProperty(value = "浏览器", index = 1)
    private String browser;

    /**
     * 操作系统
     */
    @ExcelProperty(value = "操作系统", index = 2)
    private String os;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 3)
    private String remark;

    /**
     * 操作类型
     */
    private String operationType;

    @Transform(from = "operationType", dataSource = LoginOperationTypeEnum.class)
    @ExcelProperty(value = "浏览器", index = 4)
    private String operationTypeStr;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 5)
    private Date createdTime;
}