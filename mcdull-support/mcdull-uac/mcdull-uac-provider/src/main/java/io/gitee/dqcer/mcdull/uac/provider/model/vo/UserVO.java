package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

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

    @Schema(description = "主键id")
    private Long employeeId;

    private String loginName;

    private String loginPwd;

    private String actualName;

    private Integer gender;

    private String phone;

    private Long departmentId;

    private Boolean administratorFlag;

    private String remark;

    private Integer createdBy;

    private Integer updatedBy;

}
