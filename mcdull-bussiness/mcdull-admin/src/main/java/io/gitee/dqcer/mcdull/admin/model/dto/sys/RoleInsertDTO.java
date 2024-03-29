package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class RoleInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @EnumsStrValid(value = StatusEnum.class)
    private String status;

    @NotBlank
    @Length(max = 512)
    private String name;

    @Length(max = 512)
    private String code;

    @Length(max = 512)
    private String description;

    private List<Long> menuIds;

}