package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* @author dqcer
* @since 2022-11-16
*/
@Data
public class DeptListDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private Boolean inactive;

    @Length(max = 128)
    private String name;

}