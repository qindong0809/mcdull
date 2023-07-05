package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 码表类型 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@Data
public class DictTypeAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String dictName;

    @NotBlank
    private String dictType;

    @EnumsStrValid(required = true, value = StatusEnum.class)
    private String status;

}