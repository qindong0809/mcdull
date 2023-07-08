package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 码表数据 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictDataEditDTO extends DictDataAddDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long dictCode;
}