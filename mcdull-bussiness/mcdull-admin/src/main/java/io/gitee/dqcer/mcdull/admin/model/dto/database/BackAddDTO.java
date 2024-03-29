package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
*  接收客户端参数
*
* @author dqcer
* @since 2023-08-17
*/
@Data
public class BackAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Length( min = 1, max = 512)
    private String name;

    @NotBlank
    @Length( min = 1, max = 512)
    private String remark;

    @NotNull
    private Long ticketId;

}