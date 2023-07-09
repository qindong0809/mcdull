package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 系统配置 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigEditDTO extends ConfigAddDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id;



}
