package io.gitee.dqcer.mcdull.admin.model.dto.database;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 系统配置 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupEditDTO extends GroupAddDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id;

}
