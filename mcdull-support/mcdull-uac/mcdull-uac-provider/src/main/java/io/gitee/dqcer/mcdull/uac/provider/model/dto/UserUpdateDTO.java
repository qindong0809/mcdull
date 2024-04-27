package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Getter;
import lombok.Setter;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Getter
@Setter
public class UserUpdateDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String loginPwd;

    private Integer gender;

    private String phone;

    private Long departmentId;

    private Boolean administratorFlag;

    private String remark;
}