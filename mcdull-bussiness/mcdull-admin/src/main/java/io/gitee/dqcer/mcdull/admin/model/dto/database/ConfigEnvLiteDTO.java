package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

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
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class})
     private Long id;
    /**
     * 标识（1/dev 2/test 3/prod）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer type;

    /**
     * 动态标题（1/正常 2/false）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer dynamicTitle;

    /**
     * 固定header（1/正常 2/false）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer fixedHeader;

    /**
     * 主题风格设置
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String sideTheme;

    /**
     * 显示logo（1/正常 2/false）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer sidebarLogo;

    /**
     * 开启Tags-Views（1/正常 2/false）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer tagsView;

    /**
     * 主题色
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String theme;

    /**
     * 开启TopNav（1/正常 2/false）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer topNav;

}