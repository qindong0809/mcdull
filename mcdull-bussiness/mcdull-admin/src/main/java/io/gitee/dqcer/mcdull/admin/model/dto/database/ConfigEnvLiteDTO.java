package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.admin.model.enums.EnvTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
*  接收客户端参数
*
* @author dqcer
* @since 2023-08-29
*/
@Data
public class ConfigEnvLiteDTO implements DTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     private Long id;
    /**
     * 标识（1/dev 2/test 3/prod）
     */
    @EnumsIntValid(required = true, value = EnvTypeEnum.class)
    private Integer type;

    /**
     * 动态标题（1/正常 2/false）
     */
    @NotNull
    private Boolean dynamicTitle;

    /**
     * 固定header（1/正常 2/false）
     */
    @NotNull
    private Boolean fixedHeader;

    /**
     * 主题风格设置
     */
    @NotBlank
    private String sideTheme;

    /**
     * 显示logo（1/正常 2/false）
     */
    @NotNull
    private Boolean sidebarLogo;

    /**
     * 开启Tags-Views（1/正常 2/false）
     */
    @NotNull
    private Boolean tagsView;

    /**
     * 主题色
     */
    @NotBlank
    private String theme;

    /**
     * 开启TopNav（1/正常 2/false）
     */
    @NotNull
    private Boolean topNav;

}