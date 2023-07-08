package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 码表数据 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictDataLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private String dictType;

    @Length(groups = {ValidGroup.List.class}, max = 64)
    private String dictLabel;

    @EnumsStrValid(groups = {ValidGroup.List.class}, value = StatusEnum.class)
    private String status;

}