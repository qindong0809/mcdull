package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
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

    private String loginName;

    private String loginPwd;

    private Integer gender;

    private String phone;

    private Long departmentId;

    private Boolean administratorFlag;

    private String remark;

    private Integer createdBy;

    private Integer updatedBy;

}
