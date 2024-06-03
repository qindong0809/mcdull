package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Schema
@Getter
@Setter
@ToString
public class UserInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String loginPwd;

    private Integer gender;

    private String phone;

    private Integer departmentId;

    private Boolean administratorFlag;

    private String remark;
}