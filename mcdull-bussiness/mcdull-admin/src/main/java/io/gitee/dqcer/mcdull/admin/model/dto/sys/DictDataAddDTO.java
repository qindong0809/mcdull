package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 码表数据 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@Data
public class DictDataAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String dictType;

    @Length(max = 64)
    private String dictLabel;

    @EnumsStrValid(required = true, value = StatusEnum.class)
    private String status;

    @NotBlank
    private String dictValue;

    @NotNull
    private Integer dictSort;

    private String listClass;

    private String cssClass;

    @Length(max = 128)
    private String remark;

}