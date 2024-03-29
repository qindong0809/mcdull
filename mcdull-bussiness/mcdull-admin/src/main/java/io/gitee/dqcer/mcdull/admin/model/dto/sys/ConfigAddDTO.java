package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 系统配置 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@Data
public class ConfigAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    @NotBlank
    @Length(max = 128)
    private String name;

    /**
     * 参数键名
     */
    @NotBlank
    @Length(max = 128)
    private String configKey;

    /**
     * 参数键值
     */
    @NotBlank
    @Length(max = 128)
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @NotBlank
    private String configType;

    @Length(max = 128)
    private String remark;


}