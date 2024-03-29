package io.gitee.dqcer.mcdull.admin.model.entity.database;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * database group
 *
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("database_config_env")
public class ConfigEnvEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * {@link io.gitee.dqcer.mcdull.admin.model.enums.EnvTypeEnum}
     */
    private Integer type;

    private Integer dynamicTitle;

    private Integer fixedHeader;

    private String sideTheme;

    private Integer sidebarLogo;

    private Integer tagsView;

    private String theme;

    private Integer topNav;
}
